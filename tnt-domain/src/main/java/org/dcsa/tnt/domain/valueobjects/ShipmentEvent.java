package org.dcsa.tnt.domain.valueobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dcsa.tnt.domain.valueobjects.enums.DocumentTypeCode;
import org.dcsa.tnt.domain.valueobjects.enums.ShipmentEventTypeCode;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class ShipmentEvent extends DomainEvent {
  private ShipmentEventTypeCode shipmentEventTypeCode;
  private DocumentTypeCode documentTypeCode;
  private UUID documentID;
  private String documentReference;
  private String reason;
}
