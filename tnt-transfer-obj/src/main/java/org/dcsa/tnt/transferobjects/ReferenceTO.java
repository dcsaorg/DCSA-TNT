package org.dcsa.tnt.transferobjects;

import lombok.Builder;
import org.dcsa.tnt.transferobjects.enums.ReferenceType;

import java.util.UUID;

public record ReferenceTO (
  ReferenceType referenceType,
  String referenceValue
) {
  @Builder
  public ReferenceTO { }
}
