package org.dcsa.tnt.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.tnt.persistence.entity.enums.TransportEventTypeCode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("TRANSPORT")
public class AggregatedTransportEvent extends AggregatedEvent {
  @Enumerated(EnumType.STRING)
  @Column(name = "transport_event_type_code")
  private TransportEventTypeCode transportEventTypeCode;

  @Column(name = "delay_reason_code")
  private String delayReasonCode;

  @Column(name = "change_remark")
  private String changeRemark;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "transport_call_id")
  private TransportCall transportCall;
}
