package org.dcsa.tnt.service.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.Data;
import org.dcsa.tnt.persistence.entity.enums.EventClassifierCode;
import org.dcsa.tnt.persistence.entity.enums.EventType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "eventType")
@JsonSubTypes({
  @Type(value = EquipmentEvent.class, name = "EQUIPMENT"),
  @Type(value = TransportEvent.class, name = "TRANSPORT"),
  @Type(value = ShipmentEvent.class, name = "SHIPMENT")
})
@Data
public abstract class Event {
  private UUID eventID;
  private EventType eventType;
  private OffsetDateTime eventDateTime;
  private OffsetDateTime eventCreatedDateTime;
  private EventClassifierCode eventClassifierCode;

  private List<DocumentReference> relatedDocumentReferences;
  private List<Reference> references;
}
