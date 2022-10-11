package org.dcsa.tnt.service.domain;

import lombok.Builder;

import java.util.UUID;

public record Voyage(
  UUID id,
  String carrierVoyageNumber,
  String universalVoyageReference,
  Service service
) {
  @Builder
  public Voyage {}
}
