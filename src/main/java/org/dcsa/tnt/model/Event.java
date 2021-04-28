package org.dcsa.tnt.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dcsa.core.model.AuditBase;
import org.dcsa.core.model.GetId;
import org.dcsa.tnt.model.enums.EventClassifierCode;
import org.dcsa.tnt.model.enums.EventType;
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
        @JsonSubTypes.Type(value = ShipmentEvent.class, name="SHIPMENT"),
        @JsonSubTypes.Type(value = TransportEquipmentEvent.class, name="TRANSPORTEQUIPMENT")
})
public class Event extends AuditBase implements GetId<UUID> {

    @Id
    @JsonProperty("eventID")
    @Column("event_id")
    private UUID id;


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public OffsetDateTime getEventDateTime() {
        return eventDateTime;
    }

    @JsonProperty("eventDateTime")
    @Column("event_date_time")
    private OffsetDateTime eventDateTime;

    @JsonProperty("eventClassifierCode")
    @Column("event_classifier_code")
    private EventClassifierCode eventClassifierCode;

    @JsonProperty("eventType")
    @Column("event_type")
    private EventType eventType;

    @JsonProperty("eventTypeCode")
    @Column("event_type_code")
    private String eventTypeCode;

    public void setEventClassifierCode(String eventClassifierCode) {
        this.eventClassifierCode = EventClassifierCode.valueOf(eventClassifierCode);
    }

    public void setEventClassifierCode(EventClassifierCode eventClassifierCode) {
        this.eventClassifierCode = eventClassifierCode;
    }

    public void setEventType(String eventType) {
        this.eventType = EventType.valueOf(eventType);
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
