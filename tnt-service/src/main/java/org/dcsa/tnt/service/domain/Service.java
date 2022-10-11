package org.dcsa.tnt.service.domain;

import lombok.Builder;

import java.util.UUID;

public record Service(
  UUID id,
  Carrier carrier,
  String carrierServiceCode,
  String carrierServiceName,
  String tradelaneId,
  String universalServiceReference
) {
  @Builder
  public Service {}
}
