package org.dcsa.tnt.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.core.events.model.Message;
import org.dcsa.tnt.model.EventSubscriptionState;
import org.dcsa.tnt.model.enums.SignatureMethod;
import org.dcsa.tnt.service.impl.config.MessageServiceConfig;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSignatureHandler {

    private static final String SIGNATURE_HEADER_NAME = "Notification-Signature";
    private static final String SUBSCRIPTION_ID_HEADER_NAME = "Subscription-ID";
    private static final String API_VERSION_HEADER_NAME = "API-Version";
    private static final Map<SignatureMethod, SignatureFunction> SIGNATURE_FUNCTION_MAP = new HashMap<>();

    static {
        declareKeyFunction(PlainPasswordFunction.INSTANCE);
        declareKeyFunction(HMacFunction.of(SignatureMethod.HMAC_SHA256));
    }

    private static void declareKeyFunction(SignatureFunction signatureFunction) {
        SIGNATURE_FUNCTION_MAP.put(signatureFunction.getSignatureMethod(), signatureFunction);
    }

    private final MessageServiceConfig messageServiceConfig;

    private final ObjectMapper objectMapper;

    public SignatureFunction getSignatureFunction(SignatureMethod signatureMethod) {
        return SIGNATURE_FUNCTION_MAP.get(signatureMethod);
    }

    public <T> Mono<SignatureResult<T>> verifyRequest(ServerHttpRequest request, String expectedSubscriptionID, byte[] key, Class<T> type) {
        List<String> signatureValues = request.getHeaders().get(SIGNATURE_HEADER_NAME);
        if (signatureValues == null || signatureValues.size() != 1) {
            SignatureResult<T> responsePayload;
            if (signatureValues == null || signatureValues.isEmpty()) {
                responsePayload = SignatureResult.of("Invalid; Missing " + SIGNATURE_HEADER_NAME + " header");
            } else {
                responsePayload = SignatureResult.of("Invalid; Ambiguous " + SIGNATURE_HEADER_NAME + " header");
            }
            return Mono.just(responsePayload);
        }
        String signatureLine = signatureValues.get(0).trim();
        int equalIndex = signatureLine.indexOf('=');
        if (equalIndex < 1) {
            return Mono.just(SignatureResult.of("Invalid; " + SIGNATURE_HEADER_NAME + " should use \"<signature-type>=<signature-here>\""));
        }
        String signatureType = signatureLine.substring(0, equalIndex).toLowerCase();
        String signaturePart = signatureLine.substring(equalIndex + 1);
        SignatureMethod signatureMethod = SignatureMethod.byTag(signatureType).orElse(null);
        if (signatureMethod == null) {
            return Mono.just(SignatureResult.of("Invalid; The signature type \"" + signatureType
                    + "\" in " + SIGNATURE_HEADER_NAME + " is invalid or not supported"));
        }
        SignatureFunction signatureFunction = getSignatureFunction(signatureMethod);
        if (key.length < signatureFunction.getMinKeyLength() || key.length > signatureFunction.getMaxKeyLength()) {
            return Mono.just(SignatureResult.of("Invalid; The key does not match the signature algorithm in "
                    + SIGNATURE_HEADER_NAME));
        }
        byte[] providedSignature;
        try {
            providedSignature = signatureFunction.parseSignature(signaturePart);
        } catch (Exception e) {
            return Mono.just(SignatureResult.of("Invalid; The decoding failed of the signature in "
                    + SIGNATURE_HEADER_NAME + ": " + e.getMessage()));
        }
        List<String> subscriptionIDs = request.getHeaders().get("Subscription-ID");
        if (subscriptionIDs == null || subscriptionIDs.size() != 1) {
            SignatureResult<T> responsePayload;
            if (subscriptionIDs == null || subscriptionIDs.isEmpty()) {
                responsePayload = SignatureResult.of("Invalid; Missing " + SUBSCRIPTION_ID_HEADER_NAME + " header");
            } else {
                responsePayload = SignatureResult.of("Invalid; Ambiguous " + SUBSCRIPTION_ID_HEADER_NAME + " header");
            }
            return Mono.just(responsePayload);
        }
        if (!Objects.equals(subscriptionIDs.get(0).trim(), expectedSubscriptionID)) {
            return Mono.just(SignatureResult.of("Invalid; Subscription-ID provided in " + SUBSCRIPTION_ID_HEADER_NAME
                    + " did not match the expected Subscription-ID"));
        }
        return request.getBody().map(DataBuffer::asByteBuffer).collectList()
                .flatMap(buffers -> {
                    byte[] payload;
                    if (buffers.size() == 1) {
                        ByteBuffer buffer = buffers.get(0);
                        payload = new byte[buffer.remaining()];
                        buffer.get(payload);
                    } else {
                        int total = buffers.stream().map(ByteBuffer::remaining).reduce(0, Integer::sum);
                        payload = new byte[total];
                        ByteBuffer combined = ByteBuffer.wrap(payload);
                        for (ByteBuffer subBuffer : buffers) {
                            combined.put(subBuffer);
                        }
                    }
                    SignatureResult<T> s;
                    try {
                        if (signatureFunction.verifySignature(key, payload, providedSignature)) {
                            s = SignatureResult.of("Valid",
                                    true,
                                    signatureFunction.getSignatureMethod(),
                                    objectMapper.readValue(payload, type)
                            );
                        } else {
                            s = SignatureResult.of("Invalid; The signature provided in " + SIGNATURE_HEADER_NAME
                                    + " did not match the payload (or the wrong key was used)", signatureFunction.getSignatureMethod());
                        }
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                    return Mono.just(s);
                });
    }

    public <T extends EventSubscriptionState> Mono<T> emitMessage(T eventSubscriptionState,
                                                                  Flux<? extends Message> messages) {
        int bundleSize = eventSubscriptionState.getLastBundleSize() != null
                ? eventSubscriptionState.getLastBundleSize()
                : messageServiceConfig.getDefaultBundleSize();
        URI uri;
        String callbackUrl = eventSubscriptionState.getCallbackUrl();
        try {
            uri = new URI(callbackUrl);
        } catch (URISyntaxException e) {
            IllegalArgumentException out = new IllegalArgumentException("Could not parse callback URL: " + callbackUrl, e);
            log.error(out.getLocalizedMessage(), e);
            throw out;
        }
        SignatureFunction signatureFunction = getSignatureFunction(eventSubscriptionState.getSignatureMethod());
        if (signatureFunction == null) {
            UnsupportedOperationException e = new UnsupportedOperationException("The subscription wanted signature "
                    + eventSubscriptionState.getSignatureMethod() + " but it is not known/supported!");
            log.error(e.getLocalizedMessage(), e);
            throw e;
        }
        WebClient webClient = WebClient.builder()
                // TODO: Disable redirects
                // TODO: Set time outs
                .defaultHeader("Content-Type", "application/json")
                .build();
        log.info("Extracting up to " + bundleSize + " messages to submit to " + eventSubscriptionState.getCallbackUrl());
        return messages.limitRequest(bundleSize)
                .collectList()
                .flatMap(messageBundle -> {
                    if (messageBundle.isEmpty()) {
                        return Mono.just(eventSubscriptionState);
                    }
                    byte[] bundleSerialized;
                    log.info("Submitting " + messageBundle.size() + " message(s) to subscription " + eventSubscriptionState.getCallbackUrl());

                    try {
                        bundleSerialized = objectMapper.writeValueAsBytes(messageBundle);
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException("Cannot serialize events", e);
                    }
                    final String signatureHeaderValue;
                    try {
                        signatureHeaderValue = signatureFunction.computeSignatureString(
                                eventSubscriptionState.getSigningKey(),
                                bundleSerialized
                        );
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                    return webClient.post()
                            .uri(uri)
                            .header(SIGNATURE_HEADER_NAME, signatureHeaderValue)
                            .header(SUBSCRIPTION_ID_HEADER_NAME, String.valueOf(eventSubscriptionState.getSubscriptionID()))
                            .header(API_VERSION_HEADER_NAME, messageServiceConfig.getApiSpecificationVersion())
                            .bodyValue(bundleSerialized)
                            .exchangeToMono(clientResponse -> {
                                HttpStatus httpStatus = clientResponse.statusCode();
                                String statusMessage;
                                if (httpStatus.is2xxSuccessful()) {
                                    eventSubscriptionState.resetFailureState();
                                    statusMessage = "Success. Sent " + messageBundle.size()  + " message(s), response code "
                                            + httpStatus.value();
                                    log.debug("Message sent to " + callbackUrl + ", it replied with: "
                                            + httpStatus.value() + " (status: " + statusMessage + ")");
                                } else {
                                    ClientResponse.Headers headers = clientResponse.headers();
                                    List<String> retryAfterHeader = headers.header("Retry-After");
                                    OffsetDateTime nextAttempt = null;
                                    boolean delayRequestedBySubscriber = false;
                                    boolean respectRetryAfter = false;
                                    eventSubscriptionState.setRetryCount(eventSubscriptionState.getRetryCount() + 1);
                                    if (retryAfterHeader.size() == 1) {
                                        nextAttempt = parseRetryAfterHeader(callbackUrl, retryAfterHeader.get(0));
                                        delayRequestedBySubscriber = nextAttempt != null;
                                    } else if (!retryAfterHeader.isEmpty()) {
                                        log.debug(callbackUrl + " returned multiple Retry-After headers; ignoring them all");
                                    }
                                    switch (httpStatus) {
                                        case PAYLOAD_TOO_LARGE:
                                            eventSubscriptionState.setLastBundleSize(Math.max(bundleSize / 2, 1));
                                            respectRetryAfter = true;
                                            // On a Payload too large, we retry "immediately" if we can reduce the size
                                            // and the subscriber did not ask for a delay.
                                            if (!delayRequestedBySubscriber && bundleSize > 1) {
                                                // Keep the original delay in case the reduced request fails.
                                                statusMessage = "Payload was too large (HTTP 429); retrying soon with a smaller payload";
                                            } else if (bundleSize == 1) {
                                                statusMessage = "Payload was too large (HTTP 429) but we cannot reduce the bundle size";
                                            } else {
                                                statusMessage = "Payload was too large (HTTP 429); retrying later (delay requested by subscriber)";
                                            }
                                            break;
                                        case SERVICE_UNAVAILABLE:
                                            respectRetryAfter = true;
                                            if (delayRequestedBySubscriber) {
                                                statusMessage = "Subscriber was unavailable (HTTP 503) and provided a valid Retry-After header";
                                            } else {
                                                statusMessage = "Subscriber was unavailable (HTTP 503) without a (valid) Retry-After."
                                                        + " Using back off strategy for delay";
                                            }
                                            break;
                                        case TOO_MANY_REQUESTS:
                                            respectRetryAfter = true;
                                            if (delayRequestedBySubscriber) {
                                                statusMessage = "Subscriber found us too persistent (HTTP 429) and provided a Valid Retry-After header.";
                                            } else {
                                                statusMessage = "Subscriber found us too persistent (HTTP 429) but did not provide a (valid) Retry-After."
                                                        + " Using back off strategy for delay";
                                            }
                                            break;
                                        case MOVED_PERMANENTLY:
                                        case MOVED_TEMPORARILY:
                                        case TEMPORARY_REDIRECT:
                                        case PERMANENT_REDIRECT:
                                            statusMessage = "Subscriber responded with a redirection (HTTP " + httpStatus.value() + "), but we do not support redirects. Subscriber should update their subscription instead";
                                            break;
                                        case BAD_REQUEST:
                                            statusMessage = "Subscriber rejected the response as malformed (HTTP " + httpStatus.value() + "). Could be a bug at subscriber or in client";
                                            break;
                                        case UNAUTHORIZED:
                                            statusMessage = "Subscriber rejected the payload due to missing / invalid Authentication (HTTP 401).  Might need a Secret reset";
                                            break;
                                        case FORBIDDEN:
                                            statusMessage = "Subscriber rejected the payload (HTTP 403)";
                                            break;
                                        default:
                                            if (httpStatus.is3xxRedirection()) {
                                                statusMessage = "Subscriber responded with cache code or an unknown redirection code (HTTP " + httpStatus.value() + ")!?";
                                            } else if (httpStatus.is4xxClientError()) {
                                                statusMessage = "Subscriber rejected message suggesting a client error (HTTP " + httpStatus.value() + ")";
                                            } else {
                                                statusMessage = "Subscriber was unavailable (HTTP " + httpStatus.value() + ")";
                                            }
                                            break;
                                    }
                                    if (!respectRetryAfter || nextAttempt == null) {
                                        long delay = computeNextDelay(eventSubscriptionState);
                                        eventSubscriptionState.setAccumulatedRetryDelay(delay);
                                        nextAttempt = OffsetDateTime.now().plusSeconds(delay);
                                    }
                                    eventSubscriptionState.setRetryAfter(nextAttempt);
                                    log.debug("Message for " + callbackUrl + " failed, it replied with: "
                                            + httpStatus.value() + ": Will retry after " + nextAttempt + " (status: "
                                            + statusMessage + ")");
                                }
                                eventSubscriptionState.setLastStatusMessage(statusMessage);
                                return Mono.just(eventSubscriptionState);
                            });
                });
    }

    private OffsetDateTime parseRetryAfterHeader(String callbackUrl, String retryAfterRaw) {
        OffsetDateTime parsedDateTime = null;

        if (!retryAfterRaw.isEmpty() && Character.isDigit(retryAfterRaw.charAt(0))) {
            try {
                long delay = Long.parseLong(retryAfterRaw);
                parsedDateTime = OffsetDateTime.now().plusSeconds(delay);
            } catch (NumberFormatException e) {
                log.debug(callbackUrl + " returned invalid Retry-After header (delay-variant); ignoring");
            }
        } else {
            try {
                parsedDateTime = OffsetDateTime.parse(retryAfterRaw, DateTimeFormatter.RFC_1123_DATE_TIME);
            } catch (DateTimeParseException e) {
                log.debug(callbackUrl + " returned invalid Retry-After header (date-variant); ignoring");
            }
        }
        return parsedDateTime;
    }

    private long computeNextDelay(EventSubscriptionState eventSubscriptionState) {
        Long delaySeconds = eventSubscriptionState.getAccumulatedRetryDelay();
        long limit = messageServiceConfig.getMaxRetryAfterDelay().toSeconds();
        if (delaySeconds == null) {
            delaySeconds = messageServiceConfig.getMinRetryAfterDelay().toSeconds();
        } else {
            delaySeconds *= 2;
        }
        if (delaySeconds >= limit) {
            delaySeconds = limit;
        }
        return delaySeconds;
    }

    @Getter
    private static class PlainPasswordFunction implements SignatureFunction {
        static final SignatureFunction INSTANCE = new PlainPasswordFunction();

        @Override
        public SignatureMethod getSignatureMethod() {
            return SignatureMethod.PLAINTEXT_PASSWORD;
        }

        @Override
        public byte[] computeSignature(byte[] key, byte[] payload) {
            return key;
        }

        @Override
        public byte[] parseSignature(String value) {
            return value.getBytes(StandardCharsets.UTF_8);
        }
    }

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    private static class HMacFunction implements SignatureFunction {
        private final SignatureMethod signatureMethod;

        @SneakyThrows(NoSuchAlgorithmException.class)
        @Override
        public byte[] computeSignature(byte[] key, byte[] payload) throws InvalidKeyException {
            String javaAlgorithmName = getJavaAlgorithmName();
            Mac mac = Mac.getInstance(javaAlgorithmName);
            mac.init(new SecretKeySpec(key, javaAlgorithmName));
            return mac.doFinal(payload);
        }

    }
}
