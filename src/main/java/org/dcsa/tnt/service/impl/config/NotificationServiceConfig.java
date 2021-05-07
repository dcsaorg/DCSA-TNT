package org.dcsa.tnt.service.impl.config;

import lombok.Getter;
import org.dcsa.tnt.model.enums.SignatureMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.Min;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Configuration
@ConfigurationProperties(prefix = "dcsa.event-notification-service")
public class NotificationServiceConfig {

    @Min(1)
    private int defaultBundleSize = 100;

    @DurationUnit(ChronoUnit.MINUTES)
    private Duration minRetryAfterDelay = Duration.of(60, ChronoUnit.SECONDS);

    @DurationUnit(ChronoUnit.MINUTES)
    private Duration maxRetryAfterDelay = Duration.of(24, ChronoUnit.HOURS);

    private SignatureMethod defaultSignatureMethod;

    @Autowired
    void initializeDefaultSignatureMethod(@Value("${dcsa.EventNotificationService.defaultSignatureMethod:sha256}") String defaultSignatureMethod) {
        Optional<SignatureMethod> signatureMethodOptional = SignatureMethod.byTag(defaultSignatureMethod);
        if (signatureMethodOptional.isEmpty()) {
            String methods = Arrays.stream(SignatureMethod.values())
                    .map(SignatureMethod::getSignatureMethodTag)
                    .sorted()
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Unknown signature listed in dcsa.EventNotificationService.defaultSignatureMethod: \""
                    + defaultSignatureMethod + "\". Please choose one of: " + methods );
        }
        this.defaultSignatureMethod = signatureMethodOptional.get();
    }
}
