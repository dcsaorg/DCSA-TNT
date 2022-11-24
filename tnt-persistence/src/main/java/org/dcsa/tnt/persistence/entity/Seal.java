package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.tnt.persistence.entity.enums.SealSourceCode;
import org.dcsa.tnt.persistence.entity.enums.SealType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
@Entity
public class Seal {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "utilized_transport_equipment_id", nullable = false)
  private UUID utilizedEquipmentID;

  @Column(name = "seal_number", length = 15, nullable = false)
  private String sealNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "seal_source_code")
  private SealSourceCode sealSourceCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "seal_type_code")
  private SealType sealType;
}
