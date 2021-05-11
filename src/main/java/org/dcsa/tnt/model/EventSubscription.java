package org.dcsa.tnt.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dcsa.tnt.model.base.AbstractEventSubscription;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("event_subscription")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventSubscription extends AbstractEventSubscription {

    @Column("retry_count")
    private Long retryCount = 0L;

    @Column("retry_after")
    private OffsetDateTime retryAfter;

    @Column("last_event_id")
    private UUID lastEventID;

    @Column("last_event_date_created_date_time")
    private OffsetDateTime lastEventDateCreatedDateTime;
}
