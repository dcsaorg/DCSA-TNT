package org.dcsa.tnt.service.domain;

import lombok.Builder;
import org.dcsa.tnt.persistence.entity.enums.ReferenceType;

import java.util.UUID;

public record Reference(
  UUID id,
  ReferenceType referenceType,
  String referenceValue,
  UUID shipmentID,
  UUID shippingInstructionID,
  UUID bookingID,
  UUID consignmentItemID
) {
  @Builder
  public Reference { }
}
