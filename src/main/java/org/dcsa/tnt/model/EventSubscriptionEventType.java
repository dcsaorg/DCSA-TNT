package org.dcsa.tnt.model;

import lombok.Data;
import org.dcsa.core.events.model.enums.EventType;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("event_subscription_event_types")
public class EventSubscriptionEventType {

    @Column("subscription_id")
    private UUID subscriptionID;

    @Column("event_type")
    private EventType eventType;
}
