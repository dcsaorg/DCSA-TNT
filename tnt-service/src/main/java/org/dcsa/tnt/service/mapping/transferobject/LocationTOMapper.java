package org.dcsa.tnt.service.mapping.transferobject;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.dcsa.skernel.infrastructure.transferobject.enums.FacilityCodeListProvider;
import org.dcsa.tnt.service.domain.Facility;
import org.dcsa.tnt.service.domain.Location;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class LocationTOMapper {
  private final AddressTOMapper addressTOMapper;

  public LocationTO toDTO(Location location) {
    if (location == null) {
      return null;
    }

    if (location.address() != null) {
      return LocationTO.addressLocationBuilder()
        .locationName(location.locationName())
        .address(addressTOMapper.toDomain(location.address()))
        .build();
    } else if (location.facility() != null) {
      Facility facility = location.facility();
      String facilityCode;
      FacilityCodeListProvider facilityCodeListProvider;
      if (facility.facilitySMDGCode() != null) {
        facilityCode = facility.facilitySMDGCode();
        facilityCodeListProvider = FacilityCodeListProvider.SMDG;
      } else if (facility.facilityBICCode() != null) {
        facilityCode = facility.facilityBICCode();
        facilityCodeListProvider = FacilityCodeListProvider.BIC;
      } else {
        throw new IllegalArgumentException("Facility " + facility.id()+ " has neither SMDG code nor BIC code");
      }
      return LocationTO.facilityLocationBuilder()
        .locationName(location.locationName())
        .UNLocationCode(location.UNLocationCode())
        .facilityCode(facilityCode)
        .facilityCodeListProvider(facilityCodeListProvider)
        .build();
    } else if (location.UNLocationCode() != null) {
      return LocationTO.unLocationLocationBuilder()
        .locationName(location.locationName())
        .UNLocationCode(location.UNLocationCode())
        .build();
    } else {
      throw new IllegalArgumentException("Location " + location.id() + " has neither address, facility nor unLocation");
    }
  }
}
