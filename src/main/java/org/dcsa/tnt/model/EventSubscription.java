package org.dcsa.tnt.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dcsa.core.events.model.EventSubscriptionState;
import org.dcsa.tnt.model.base.AbstractEventSubscription;
import org.dcsa.core.events.model.enums.SignatureMethod;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Table("event_subscription")
@Data
@EqualsAndHashCode(callSuper = true)
public class  EventSubscription extends AbstractEventSubscription implements EventSubscriptionState {

    @Column("retry_count")
    private Long retryCount = 0L;

    @Column("retry_after")
    private OffsetDateTime retryAfter;

    @Column("accumulated_retry_delay")
    private Long accumulatedRetryDelay;

    @Column("last_bundle_size")
    private Integer lastBundleSize;

    @Column("signature_method")
    private SignatureMethod signatureMethod;

    public byte[] getSigningKey() {
        return secret;
    }

    public void copyInternalFieldsFrom(EventSubscription eventSubscription) {
        this.retryCount = eventSubscription.retryCount;
        this.retryAfter = eventSubscription.retryAfter;
        this.accumulatedRetryDelay = eventSubscription.accumulatedRetryDelay;
        this.signatureMethod = eventSubscription.signatureMethod;
    }
}
