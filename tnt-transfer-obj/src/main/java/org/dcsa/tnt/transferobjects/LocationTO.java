package org.dcsa.tnt.transferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dcsa.tnt.transferobjects.jackson.LocationTODeserializer;

import javax.validation.constraints.Size;

@JsonDeserialize(using = LocationTODeserializer.class)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class LocationTO {
  @Size(max = 100) String locationName;
}
