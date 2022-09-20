package org.dcsa.tnt.transferobjects;

import lombok.Builder;
import org.dcsa.tnt.transferobjects.enums.DocumentReferenceType;

public record DocumentReferenceTO (
  DocumentReferenceType documentReferenceType,
  String documentReferenceValue
) {
  @Builder
  public DocumentReferenceTO {}
}
