package org.dcsa.tnt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dcsa.core.model.AuditBase;
import org.dcsa.core.model.GetId;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("event_subscription")
@Data
@NoArgsConstructor
public class EventSubscription extends AuditBase implements GetId<UUID> {

    @Id
    @JsonProperty("subscriptionID")
    @Column("subscription_id")
    private UUID id;

    @JsonProperty("callbackUrl")
    @Column("callback_url")
    private String callbackUrl;

    @JsonIgnore
    @Column("event_type")
    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("bookingReference")
    @Column("booking_reference")
    private String bookingReference;

    @JsonProperty("billOfLadingNumber")
    @Column("bill_of_lading_number")
    private String billOfLadingNumber;

    @JsonProperty("equipmentReference")
    @Column("equipment_reference")
    private String equipmentReference;
}
