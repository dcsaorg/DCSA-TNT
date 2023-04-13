package org.dcsa.tnt.domain.valueobjects;

import lombok.Builder;

import jakarta.validation.constraints.Size;
import java.util.UUID;

public record Address(
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
