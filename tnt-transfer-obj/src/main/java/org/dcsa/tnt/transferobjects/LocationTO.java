package org.dcsa.tnt.transferobjects;

import lombok.Builder;
import org.dcsa.tnt.transferobjects.enums.FacilityCodeListProvider;

import javax.validation.constraints.Size;

public record LocationTO(
  @Size(max = 100) String locationName,
  @Size(max = 10) String latitude,
  @Size(max = 11) String longitude,
  @Size(max = 5) String UNLocationCode,
  @Size(max = 6) String facilityCode,
  FacilityCodeListProvider facilityCodeListProvider,
  AddressTO address
) {
  @Builder(toBuilder = true) // workaround for intellij issue
  public LocationTO { }
}
