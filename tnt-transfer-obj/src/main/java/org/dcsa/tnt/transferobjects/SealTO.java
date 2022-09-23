package org.dcsa.tnt.transferobjects;

import lombok.Builder;
import org.dcsa.tnt.transferobjects.enums.SealSourceCode;
import org.dcsa.tnt.transferobjects.enums.SealType;

import java.util.UUID;

public record SealTO(
  UUID utilizedEquipmentID,
  String sealNumber,
  SealSourceCode sealSourceCode,
  SealType sealType
) {
  @Builder
  public SealTO {}
}
