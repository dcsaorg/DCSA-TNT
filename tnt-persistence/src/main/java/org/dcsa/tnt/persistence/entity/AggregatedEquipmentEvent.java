package org.dcsa.tnt.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("EQUIPMENT")
public class AggregatedEquipmentEvent extends AggregatedEvent {
  @Column(name = "equipment_reference")
  private String equipmentReference;
}
