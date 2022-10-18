package org.dcsa.tnt.transferobjects;

import lombok.Builder;
import org.dcsa.tnt.transferobjects.enums.ReferenceType;

import java.util.UUID;

public record ReferenceTO (
  ReferenceType type,
  String value
) {
  @Builder
  public ReferenceTO { }
}
