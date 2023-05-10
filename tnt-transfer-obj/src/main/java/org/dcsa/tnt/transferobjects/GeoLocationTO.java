package org.dcsa.tnt.transferobjects;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class GeoLocationTO extends LocationTO {
  @Size(max = 11)
  private String latitude;

  @Size(max = 11)
  private String longitude;
}
