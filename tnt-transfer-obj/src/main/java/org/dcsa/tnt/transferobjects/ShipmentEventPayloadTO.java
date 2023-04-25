package org.dcsa.tnt.transferobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dcsa.tnt.transferobjects.enums.DocumentTypeCode;
import org.dcsa.tnt.transferobjects.enums.ShipmentEventTypeCode;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class ShipmentEventPayloadTO extends EventPayloadTO {
  private ShipmentEventTypeCode shipmentEventTypeCode;
  private DocumentTypeCode documentTypeCode;
  private String documentReference;
  private String reason;
}
