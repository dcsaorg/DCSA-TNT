package org.dcsa.tnt.service.domain;

import lombok.Builder;

import javax.validation.constraints.Size;
import java.util.UUID;

public record Address(
  UUID id,
  @Size(max = 100) String name,
  @Size(max = 100) String street,
  @Size(max = 50) String streetNumber,
  @Size(max = 50) String floor,
  @Size(max = 10) String postCode,
  @Size(max = 65) String city,
  @Size(max = 65) String stateRegion,
  @Size(max = 75) String country
) {
  @Builder // workaround for intellij issue
  public Address { }
}
