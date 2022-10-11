package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.tnt.persistence.entity.enums.WeightUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "equipment")
public class Equipment {
  @Id
  @Column(name = "equipment_reference", length = 15, nullable = false)
  private String equipmentReference;

  @Column(name = "iso_equipment_code", length = 4, columnDefinition = "bpchar")
  private String ISOEquipmentCode;

  @Column(name = "tare_weight")
  private Double tareWeight;

  @Enumerated(EnumType.STRING)
  @Column(name = "weight_unit")
  private WeightUnit weightUnit;
}
