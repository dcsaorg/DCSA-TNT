package org.dcsa.tnt.transferobjects;

import lombok.Data;
import lombok.ToString;
import org.dcsa.tnt.transferobjects.enums.DocumentTypeCode;
import org.dcsa.tnt.transferobjects.enums.ShipmentEventTypeCode;

import java.util.List;
import java.util.UUID;

@Data
@ToString(callSuper = true)
public class ShipmentEventTO extends EventTO {
  private ShipmentEventTypeCode shipmentEventTypeCode;

  private DocumentTypeCode documentTypeCode;

  private UUID documentID;

  private String reason;

  private List<ReferenceTO> references;
}
