package org.dcsa.tnt.domain.valueobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dcsa.tnt.domain.valueobjects.enums.TransportEventTypeCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class TransportEvent extends DomainEvent {
  private TransportEventTypeCode transportEventTypeCode;
  private String delayReasonCode;
  private String changeRemark;
  private TransportCall transportCall;
}
