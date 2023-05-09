package org.dcsa.tnt.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.dcsa.skernel.infrastructure.transferobject.enums.FacilityCodeListProvider;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class FacilityLocationTO extends LocationTO {
  @JsonProperty("UNLocationCode")
  @Size(max = 5)
  private String unLocationCode;

  @Size(max = 6)
  private String facilityCode;

  private FacilityCodeListProvider facilityCodeListProvider;
}
