package org.dcsa.tnt.transferobjects;

import lombok.Data;
import lombok.ToString;
import org.dcsa.tnt.transferobjects.enums.TransportEventTypeCode;

import java.util.List;

@Data
@ToString(callSuper = true)
public class TransportEventTO extends EventTO {
  private TransportEventTypeCode transportEventTypeCode;
  private String delayReasonCode;
  private String changeRemark;
  private TransportCallTO transportCall;

  private List<DocumentReferenceTO> documentReferences;
  private List<ReferenceTO> references;
}
