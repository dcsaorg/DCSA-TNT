package org.dcsa.tnt.transferobjects;

import lombok.Builder;
import org.dcsa.tnt.transferobjects.enums.SealSourceCode;
import org.dcsa.tnt.transferobjects.enums.SealType;

import java.util.UUID;

public record SealTO(
  String number, // <-
  SealSourceCode source,
  SealType type
) {
  @Builder
  public SealTO {}
}
