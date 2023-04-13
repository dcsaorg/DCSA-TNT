package org.dcsa.tnt.domain.valueobjects;

import lombok.Builder;
import org.dcsa.tnt.domain.valueobjects.enums.SealSourceCode;
import org.dcsa.tnt.domain.valueobjects.enums.SealType;

public record Seal(
  String number,
  SealSourceCode source,
  SealType type
) {
  @Builder
  public Seal {}
}
