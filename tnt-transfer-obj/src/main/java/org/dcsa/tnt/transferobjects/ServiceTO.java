package org.dcsa.tnt.transferobjects;

import lombok.Builder;

import java.util.UUID;

public record ServiceTO(
  UUID id,

  CarrierTO carrier,

  String carrierServiceCode,

  String carrierServiceName,

  String tradelaneId,

  String universalServiceReference
) {
  @Builder
  public ServiceTO {}
}
