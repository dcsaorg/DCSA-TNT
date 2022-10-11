package org.dcsa.tnt.transferobjects.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.dcsa.tnt.transferobjects.AddressLocationTO;
import org.dcsa.tnt.transferobjects.AddressTO;
import org.dcsa.tnt.transferobjects.FacilityLocationTO;
import org.dcsa.tnt.transferobjects.LocationTO;
import org.dcsa.tnt.transferobjects.UNLocationLocationTO;
import org.dcsa.tnt.transferobjects.enums.FacilityCodeListProvider;

import java.io.IOException;

/**
 * Need a custom deserializer for LocationTO since there is no discriminator and the fields
 * are not unique enough for Jackson to figure it out automagically (spelling intended).
 */
public class LocationTODeserializer extends StdDeserializer<LocationTO> {
  public LocationTODeserializer() {
    super(LocationTO.class);
  }

  @Override
  public LocationTO deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
    CombinedLocation combinedLocation = jp.readValueAs(CombinedLocation.class);
    if (combinedLocation.address != null) {
      return AddressLocationTO.builder()
        .locationName(combinedLocation.locationName)
        .address(combinedLocation.address)
        .build();
    } else if (combinedLocation.facilityCode != null) {
      return FacilityLocationTO.builder()
        .locationName(combinedLocation.locationName)
        .facilityCode(combinedLocation.facilityCode)
        .facilityCodeListProvider(combinedLocation.facilityCodeListProvider)
        .UNLocationCode(combinedLocation.UNLocationCode())
        .build();
    } else {
      return UNLocationLocationTO.builder()
        .locationName(combinedLocation.locationName)
        .UNLocationCode(combinedLocation.UNLocationCode())
        .build();
    }
  }

  private record CombinedLocation(
    String locationName,
    @JsonProperty("UNLocationCode") String UNLocationCode,
    String facilityCode,
    FacilityCodeListProvider facilityCodeListProvider,
    AddressTO address
  ) { }
}
