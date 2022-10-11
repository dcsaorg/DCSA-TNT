package org.dcsa.tnt.service.domain;

import lombok.Builder;

import javax.validation.constraints.Size;
import java.util.UUID;

public record Location(
  UUID id,
  @Size(max = 100) String locationName,
  @Size(max = 10) String latitude,
  @Size(max = 11) String longitude,
  @Size(max = 5) String UNLocationCode,
  Address address,
  Facility facility
) {
  @Builder(toBuilder = true) // workaround for intellij issue
  public Location { }
}
