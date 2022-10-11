package org.dcsa.tnt.service.domain;

import lombok.Builder;
import org.dcsa.tnt.persistence.entity.enums.WeightUnit;

public record Equipment(
  String equipmentReference,
  String ISOEquipmentCode,
  Double tareWeight,
  WeightUnit weightUnit
) {
  @Builder
  public Equipment { }
}
