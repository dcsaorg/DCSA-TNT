package org.dcsa.tnt.domain.valueobjects;

import lombok.Builder;
import org.dcsa.tnt.domain.valueobjects.enums.DocumentReferenceType;

public record DocumentReference(
  DocumentReferenceType type,
  String value
) {
  @Builder
  public DocumentReference {}
}
