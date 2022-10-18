package org.dcsa.tnt.service.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.dcsa.tnt.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.tnt.persistence.entity.enums.EventType;
import org.dcsa.tnt.persistence.entity.enums.ShipmentEventTypeCode;

import java.util.UUID;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ShipmentEvent extends Event {
  private ShipmentEventTypeCode shipmentEventTypeCode;
  private DocumentTypeCode documentTypeCode;
  private UUID documentID;
  private String documentReference;
  private String reason;

  public ShipmentEvent() {
    setEventType(EventType.SHIPMENT);
  }
}
