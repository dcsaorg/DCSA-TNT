package org.dcsa.tnt.service.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.dcsa.tnt.persistence.entity.enums.EventType;
import org.dcsa.tnt.persistence.entity.enums.TransportEventTypeCode;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TransportEvent extends Event {
  private TransportEventTypeCode transportEventTypeCode;
  private String delayReasonCode;
  private String changeRemark;
  private TransportCall transportCall;

  public TransportEvent() {
    setEventType(EventType.TRANSPORT);
  }
}
