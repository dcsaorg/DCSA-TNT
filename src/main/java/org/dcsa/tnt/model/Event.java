package org.dcsa.tnt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dcsa.core.model.AuditBase;
import org.dcsa.tnt.model.enums.EventClassifierCode;
import org.dcsa.tnt.model.enums.EventType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("aggregated_events")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "eventType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EquipmentEvent.class, name="EQUIPMENT"),
        @JsonSubTypes.Type(value = TransportEvent.class, name="TRANSPORT"),
        @JsonSubTypes.Type(value = ShipmentEvent.class, name="SHIPMENT")
})
public class Event extends AuditBase implements Notification {

    @Id
    @Column("event_id")
    private UUID eventID;

    @Column("event_type")
    private EventType eventType;

    @Column("event_date_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime eventDateTime;

    @Column("event_created_date_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @CreatedDate
    private OffsetDateTime eventCreatedDateTime;

    @Column("event_classifier_code")
    private EventClassifierCode eventClassifierCode;

    @Column("event_type_code")
    private String eventTypeCode;
}
