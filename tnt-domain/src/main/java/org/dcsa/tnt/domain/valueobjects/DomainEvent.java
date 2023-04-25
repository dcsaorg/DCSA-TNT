package org.dcsa.tnt.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dcsa.tnt.domain.valueobjects.enums.EventClassifierCode;
import org.dcsa.tnt.domain.valueobjects.enums.EventType;

import java.time.OffsetDateTime;
import java.util.List;

@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "class")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public sealed abstract class DomainEvent permits EquipmentEvent, TransportEvent, ShipmentEvent, RetractedEvent {
  private String eventID;
  private EventType eventType;
  private OffsetDateTime eventDateTime;
  private OffsetDateTime eventCreatedDateTime;
  private EventClassifierCode eventClassifierCode;

  private List<DocumentReference> relatedDocumentReferences;
  private List<Reference> references;
}
