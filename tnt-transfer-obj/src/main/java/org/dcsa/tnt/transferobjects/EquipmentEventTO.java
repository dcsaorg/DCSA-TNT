package org.dcsa.tnt.transferobjects;

import lombok.Data;
import lombok.ToString;
import org.dcsa.tnt.transferobjects.enums.EmptyIndicatorCode;
import org.dcsa.tnt.transferobjects.enums.EquipmentEventTypeCode;

import java.util.List;

@Data
@ToString(callSuper = true)
public class EquipmentEventTO extends EventTO implements EventTOWithTransportCallTO {
  private EquipmentEventTypeCode equipmentEventTypeCode;
  private String equipmentReference;
  private EmptyIndicatorCode emptyIndicatorCode;
  private TransportCallTO transportCall;
  private LocationTO eventLocation;

  private List<DocumentReferenceTO> relatedDocumentReferences;
  private List<ReferenceTO> references;
  private List<SealTO> seals;
}
