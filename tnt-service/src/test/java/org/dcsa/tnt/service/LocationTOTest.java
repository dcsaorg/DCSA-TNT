package org.dcsa.tnt.service;

import org.dcsa.skernel.infrastructure.transferobject.AddressTO;
import org.dcsa.tnt.transferobjects.LocationTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTOTest {

  @Test
  public void testAddressLocationType() {
    AddressTO address = AddressTO.builder().build();
    LocationTO location = LocationTO.builder().address(address).build();
    assertEquals(LocationTO.LocationType.ADDR.name(), location.getLocationType());
  }

  @Test
  public void testLatitudeAndLongitudeLocationType() {
    LocationTO location = LocationTO.builder().latitude("53.551° N").longitude("9.9937° E").build();
    assertEquals(LocationTO.LocationType.GEO.name(), location.getLocationType());
  }

  @Test
  public void testFacilityCodeLocationType() {
    LocationTO location = LocationTO.builder().facilityCode("DPWJA").build();
    assertEquals(LocationTO.LocationType.FACI.name(), location.getLocationType());
  }

  @Test
  public void testUNLocationCodeLocationType() {
    LocationTO location = LocationTO.builder().UNLocationCode("USNYC").build();
    assertEquals(LocationTO.LocationType.UNLO.name(), location.getLocationType());
  }

  @Test
  public void testAddressAndGeoLocationType() {
    AddressTO address = AddressTO.builder().build();
    LocationTO location = LocationTO.builder().facilityCode("DPWJA").address(address).build();
    assertEquals(LocationTO.LocationType.ADDR.name(), location.getLocationType());
  }

  @Test
  public void testFacilityCodeAndGeoLocationType() {
    AddressTO address = AddressTO.builder().build();
    LocationTO location =
        LocationTO.builder()
            .facilityCode("DPWJA")
            .latitude("53.551° N")
            .longitude("9.9937° E")
            .build();
    assertEquals(LocationTO.LocationType.GEO.name(), location.getLocationType());
  }
}
