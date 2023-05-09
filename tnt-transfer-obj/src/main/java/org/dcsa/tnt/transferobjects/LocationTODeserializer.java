package org.dcsa.tnt.transferobjects;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.dcsa.skernel.infrastructure.transferobject.AddressTO;
import org.dcsa.skernel.infrastructure.transferobject.enums.FacilityCodeListProvider;
import org.dcsa.tnt.transferobjects.enums.LocationType;


// Had to use a custom deserializer as the @JsonTypeInfo(use = Id.DEDUCTION) did not work.
public class LocationTODeserializer extends JsonDeserializer<LocationTO> {
  @Override
  public LocationTO deserialize(
      JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JacksonException {
    JsonNode node = jsonParser.readValueAsTree();
    String locationType = node.get("locationType").asText();
    String locationName = node.get("locationName").asText("");

    return switch (locationType) {
      case "UNLO" -> UnLocationLocationTO.builder()
          .locationName(locationName)
          .locationType(LocationType.UNLO)
          .unLocationCode(node.get("UNLocationCode").asText(""))
          .build();
      case "FACI" -> FacilityLocationTO.builder()
          .locationName(locationName)
          .locationType(LocationType.FACI)
          .unLocationCode(node.get("UNLocationCode").asText(""))
          .facilityCode(node.get("facilityCode").asText(""))
          .facilityCodeListProvider(
              FacilityCodeListProvider.valueOf(node.get("facilityCodeListProvider").asText("")))
          .build();
      case "ADDR" -> {
        AddressLocationTO addressLocationTO =
            AddressLocationTO.builder()
                .locationName(locationName)
                .locationType(LocationType.ADDR)
                .build();

        if (node.hasNonNull("address")) {
          JsonNode aNode = node.get("address");
          yield addressLocationTO.toBuilder()
              .address(
                  AddressTO.builder()
                      .name(aNode.get("name").asText(""))
                      .street(aNode.get("street").asText(""))
                      .streetNumber(aNode.get("streetNumber").asText(""))
                      .floor(aNode.get("floor").asText(""))
                      .postCode(aNode.get("postCode").asText(""))
                      .city(aNode.get("city").asText(""))
                      .stateRegion(aNode.get("stateRegion").asText(""))
                      .country(aNode.get("country").asText(""))
                      .build())
              .build();
        } else {
          yield addressLocationTO;
        }
      }
      case "GEOL" -> GeoLocationTO.builder()
          .locationName(locationName)
          .locationType(LocationType.GEOL)
          .latitude(node.get("latitude").asText(""))
          .longitude(node.get("longitude").asText(""))
          .build();
      default -> throw new RuntimeException("Invalid location instance.");
    };
  }
}
