package org.dcsa.tnt.domain.valueobjects;

import lombok.Builder;
import org.dcsa.tnt.domain.valueobjects.enums.ReferenceType;

import java.util.UUID;

public record Reference(
  ReferenceType type,
  String value
) {
  @Builder
  public Reference { }
}
