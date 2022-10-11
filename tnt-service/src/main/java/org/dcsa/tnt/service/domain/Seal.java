package org.dcsa.tnt.service.domain;

import lombok.Builder;
import org.dcsa.tnt.persistence.entity.enums.SealSourceCode;
import org.dcsa.tnt.persistence.entity.enums.SealType;

import java.util.UUID;

public record Seal(
  UUID id,
  UUID utilizedEquipmentID,
  String sealNumber,
  SealSourceCode sealSourceCode,
  SealType sealType
) {
  @Builder
  public Seal {}
}
