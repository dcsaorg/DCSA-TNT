package org.dcsa.tnt.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.dcsa.skernel.infrastructure.transferobject.AddressTO;
import org.dcsa.skernel.infrastructure.transferobject.enums.FacilityCodeListProvider;

@Builder
@Data
public class LocationTO {
  @Size(max = 100)
  private String locationName;

  @Size(max = 10)
  private String latitude;

  @Size(max = 11)
  private String longitude;

  @Size(max = 5)
  @JsonProperty("UNLocationCode")
  private String UNLocationCode;

  private String locationType;
  private AddressTO address;
  private String facilityCode;
  private FacilityCodeListProvider facilityCodeListProvider;

  public static LocationTOBuilder builder() {
    return new CustomLocationTOBuilder();
  }

  private static class CustomLocationTOBuilder extends LocationTOBuilder {

    // This method overrides the build method invoked by the lombok Builder
    @Override
    public LocationTO build() {
      LocationTO locationTO = super.build();

      // Setting the location type according to the conditions
      if (locationTO.getAddress() != null) {
        locationTO.setLocationType(String.valueOf(LocationType.ADDR));
        return locationTO;
      }
      if (locationTO.getLatitude() != null && locationTO.getLongitude() != null) {
        locationTO.setLocationType(String.valueOf(LocationType.GEOL));
        return locationTO;
      }
      if (locationTO.getFacilityCode() != null) {
        locationTO.setLocationType(String.valueOf(LocationType.FACI));
        return locationTO;
      }
      if (locationTO.getUNLocationCode() != null) {
        locationTO.setLocationType(String.valueOf(LocationType.UNLO));
        return locationTO;
      }
      return locationTO;
    }
  }

  public enum LocationType {
    ADDR,
    UNLO,
    FACI,
    GEOL;
  }
}
