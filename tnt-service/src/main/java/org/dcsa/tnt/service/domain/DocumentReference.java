package org.dcsa.tnt.service.domain;

import lombok.Builder;
import org.dcsa.tnt.persistence.entity.enums.DocumentReferenceType;

public record DocumentReference(
  DocumentReferenceType type,
  String value
) {
  @Builder
  public DocumentReference {}
}
