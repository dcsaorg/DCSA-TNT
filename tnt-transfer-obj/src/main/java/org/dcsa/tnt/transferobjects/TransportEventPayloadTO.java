package org.dcsa.tnt.transferobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dcsa.tnt.transferobjects.EventPayloadTO.EventPayloadTOWithTransportCall;
import org.dcsa.tnt.transferobjects.enums.TransportEventTypeCode;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class TransportEventPayloadTO extends EventPayloadTO implements EventPayloadTOWithTransportCall {
  private TransportEventTypeCode transportEventTypeCode;
  private String delayReasonCode;
  private String changeRemark;
  private TransportCallTO transportCall;
}
