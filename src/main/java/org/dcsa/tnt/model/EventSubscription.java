package org.dcsa.tnt.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dcsa.tnt.model.base.AbstractEventSubscription;
import org.dcsa.tnt.model.enums.SignatureMethod;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("event_subscription")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventSubscription extends AbstractEventSubscription implements EventSubscriptionState {

    @Column("retry_count")
    private Long retryCount = 0L;

    @Column("retry_after")
    private OffsetDateTime retryAfter;

    @Column("last_event_id")
    private UUID lastEventID;

    @Column("last_bundle_size")
    private Integer lastBundleSize;

    @Column("last_status_message")
    private String lastStatusMessage;

    @Column("last_event_date_created_date_time")
    private OffsetDateTime lastEventDateCreatedDateTime;

    @Column("accumulated_retry_delay")
    private Long accumulatedRetryDelay;

    @Column("signature_method")
    private SignatureMethod signatureMethod;

    public byte[] getSigningKey() {
        return secret;
    }

    public void copyInternalFieldsFrom(EventSubscription eventSubscription) {
        this.retryCount = eventSubscription.retryCount;
        this.retryAfter = eventSubscription.retryAfter;
        this.lastEventID = eventSubscription.lastEventID;
        this.lastBundleSize = eventSubscription.lastBundleSize;
        this.lastStatusMessage = eventSubscription.lastStatusMessage;
        this.lastEventDateCreatedDateTime = eventSubscription.lastEventDateCreatedDateTime;
        this.accumulatedRetryDelay = eventSubscription.accumulatedRetryDelay;
        this.signatureMethod = eventSubscription.signatureMethod;
    }
}
