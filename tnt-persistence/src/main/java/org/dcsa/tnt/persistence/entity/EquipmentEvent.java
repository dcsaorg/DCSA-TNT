package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.tnt.persistence.entity.enums.EmptyIndicatorCode;
import org.dcsa.tnt.persistence.entity.enums.EquipmentEventTypeCode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "equipment_event")
public class EquipmentEvent extends Event implements EventWithTransportCall {
  @Enumerated(EnumType.STRING)
  @Column(name = "equipment_event_type_code")
  private EquipmentEventTypeCode equipmentEventTypeCode;

  @Column(name = "equipment_reference")
  private String equipmentReference;

  @Enumerated(EnumType.STRING)
  @Column(name = "empty_indicator_code")
  private EmptyIndicatorCode emptyIndicatorCode;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "transport_call_id")
  private TransportCall transportCall;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "event_location_id")
  private Location eventLocation;
}
