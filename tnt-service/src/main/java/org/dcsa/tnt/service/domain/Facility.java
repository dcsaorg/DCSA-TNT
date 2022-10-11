package org.dcsa.tnt.service.domain;

import lombok.Builder;

import java.util.UUID;

public record Facility(
  UUID id,
  String facilityName,
  String UNLocationCode,
  String facilityBICCode,
  String facilitySMDGCode,
  Location location
) {
  @Builder
  public Facility { }
}
