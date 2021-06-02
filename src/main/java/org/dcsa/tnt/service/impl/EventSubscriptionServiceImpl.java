package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.exception.CreateException;
import org.dcsa.core.exception.UpdateException;
import org.dcsa.core.service.impl.ExtendedBaseServiceImpl;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.tnt.model.Notification;
import org.dcsa.tnt.model.enums.SignatureMethod;
import org.dcsa.tnt.repository.EventSubscriptionRepository;
import org.dcsa.tnt.service.EventSubscriptionService;
import org.dcsa.tnt.service.impl.config.NotificationServiceConfig;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventSubscriptionServiceImpl extends ExtendedBaseServiceImpl<EventSubscriptionRepository, EventSubscription, UUID> implements EventSubscriptionService {
    private final EventSubscriptionRepository eventSubscriptionRepository;
    private final NotificationServiceConfig notificationServiceConfig;
    private final NotificationSignatureHandler notificationSignatureHandler;


    @Override
    public EventSubscriptionRepository getRepository() {
        return eventSubscriptionRepository;
    }

    @Override
    protected Mono<EventSubscription> preCreateHook(EventSubscription eventSubscription) {
        byte[] secret = eventSubscription.getSecret();
        SignatureMethod signatureMethod;
        if (eventSubscription.getSignatureMethod() == null) {
            signatureMethod = notificationServiceConfig.getDefaultSignatureMethod();
            eventSubscription.setSignatureMethod(signatureMethod);
        } else {
            signatureMethod = eventSubscription.getSignatureMethod();
        }
        if (secret == null || secret.length < 1) {
            return Mono.error(new CreateException("Please provide a non-empty \"secret\" attribute"));
        }
        if (signatureMethod.getMinKeyLength() == signatureMethod.getMaxKeyLength()) {
            if (secret.length != signatureMethod.getMinKeyLength()) {
                if (secret.length  > 1 && secret.length - 1 == signatureMethod.getMinKeyLength()
                        && Character.isSpaceChar(((int)secret[secret.length - 1]) & 0xff)) {
                    return Mono.error(new CreateException("The secret provided in the \"secret\" attribute must be exactly "
                            + signatureMethod.getMinKeyLength() + " bytes long (when deserialized).  Did you"
                            + " accidentally include a trailing newline/space in the secret?"));
                }
                return Mono.error(new CreateException("The secret provided in the \"secret\" attribute must be exactly "
                        + signatureMethod.getMinKeyLength() + " bytes long (when deserialized)"));
            }
        } else {
            if (secret.length < signatureMethod.getMinKeyLength()) {
                return Mono.error(new CreateException("The secret provided in the \"secret\" attribute must be at least "
                        + signatureMethod.getMinKeyLength() + " bytes long (when deserialized)"));
            }
            if (secret.length > signatureMethod.getMaxKeyLength()) {
                return Mono.error(new CreateException("The secret provided in the \"secret\" attribute must be at most "
                        + signatureMethod.getMaxKeyLength() + " bytes long (when deserialized)"));
            }
        }
        return checkCallbackUrl(eventSubscription);
    }

    @Override
    protected Mono<EventSubscription> preUpdateHook(EventSubscription original, EventSubscription update) {
        return checkCallbackUrl(update);
    }

    protected Mono<EventSubscription> checkCallbackUrl(EventSubscription eventSubscription) {
        // Ensure that the callback url at least looks valid.
        try {
            new URI(eventSubscription.getCallbackUrl());
        } catch (URISyntaxException e) {
            return Mono.error(new UpdateException("callbackUrl is invalid: " + e.getLocalizedMessage()));
        }
        return Mono.just(eventSubscription);
    }




    @Override
    public Mono<EventSubscription> emitNotification(EventSubscription eventSubscription,
                                                    Flux<? extends Notification> notifications) {
        return notificationSignatureHandler.emitNotifications(eventSubscription, notifications)
                .flatMap(this::save);
    }
}
