package org.dcsa.tnt.transferobjects;

import lombok.Builder;

public record FacilityTO(
  String facilityName,
  String UNLocationCode,
  String facilityBICCode,
  String facilitySMDGCode,
  LocationTO location
) {
  @Builder
  public FacilityTO { }
}
