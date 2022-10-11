package org.dcsa.tnt.service.mapping.transferobject;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.tnt.service.domain.Facility;
import org.dcsa.tnt.service.domain.Location;
import org.dcsa.tnt.transferobjects.AddressLocationTO;
import org.dcsa.tnt.transferobjects.FacilityLocationTO;
import org.dcsa.tnt.transferobjects.LocationTO;
import org.dcsa.tnt.transferobjects.UNLocationLocationTO;
import org.dcsa.tnt.transferobjects.enums.FacilityCodeListProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class LocationMapper {
  private final AddressMapper addressMapper;

  public LocationTO toDTO(Location location) {
    if (location == null) {
      return null;
    }

    if (location.address() != null) {
      return AddressLocationTO.builder()
        .locationName(location.locationName())
        .address(addressMapper.toDomain(location.address()))
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
        log.error("Facility {} has neither SMDG code nor BIC code", facility.id());
        return null;
      }
      return FacilityLocationTO.builder()
        .locationName(location.locationName())
        .UNLocationCode(location.UNLocationCode())
        .facilityCode(facilityCode)
        .facilityCodeListProvider(facilityCodeListProvider)
        .build();
    } else if (location.UNLocationCode() != null) {
      return UNLocationLocationTO.builder()
        .locationName(location.locationName())
        .UNLocationCode(location.UNLocationCode())
        .build();
    } else {
      log.error("Location {} has neither address, facility nor unLocation", location.id());
      return null;
    }
  }
}
