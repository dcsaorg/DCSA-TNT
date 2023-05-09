package org.dcsa.tnt.transferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dcsa.tnt.transferobjects.enums.LocationType;

@JsonDeserialize(using = LocationTODeserializer.class)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract sealed class LocationTO
    permits UnLocationLocationTO, FacilityLocationTO, AddressLocationTO, GeoLocationTO {
  @Size(max = 100)
  private String locationName;

  private LocationType locationType;
}
