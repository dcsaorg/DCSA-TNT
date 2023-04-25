package org.dcsa.tnt.domain.valueobjects;

import lombok.Builder;
import org.dcsa.skernel.infrastructure.transferobject.enums.FacilityCodeListProvider;

import jakarta.validation.constraints.Size;

public record Location(
  @Size(max = 100) String locationName,
  @Size(max = 10) String latitude,
  @Size(max = 11) String longitude,
  @Size(max = 5) String UNLocationCode,
  Address address,
  String facilityCode,
  FacilityCodeListProvider facilityCodeListProvider
) {
  @Builder(toBuilder = true) // workaround for intellij issue
  public Location { }
}
