package org.dcsa.tnt.transferobjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dcsa.tnt.transferobjects.enums.EventClassifierCode;

import java.time.OffsetDateTime;
import java.util.List;

@JsonTypeInfo(use = Id.DEDUCTION)
@JsonSubTypes({
  @Type(value = EquipmentEventPayloadTO.class),
  @Type(value = TransportEventPayloadTO.class),
  @Type(value = ShipmentEventPayloadTO.class)
})
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class EventPayloadTO {
  private OffsetDateTime eventDateTime;
  private EventClassifierCode eventClassifierCode;

  private List<DocumentReferenceTO> relatedDocumentReferences;
  private List<ReferenceTO> references;

  public interface EventPayloadTOWithTransportCall {
    TransportCallTO getTransportCall();
  }
}
