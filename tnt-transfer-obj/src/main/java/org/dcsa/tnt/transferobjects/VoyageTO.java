package org.dcsa.tnt.transferobjects;

import lombok.Builder;

import java.util.UUID;

public record VoyageTO(
  UUID id,
  String carrierVoyageNumber,
  String universalVoyageReference,
  ServiceTO service
) {
  @Builder
  public VoyageTO {}
}
