package org.dcsa.tnt.service.domain;

import lombok.Builder;

import java.util.UUID;

public record Carrier(
  UUID id,
  String carrierName,
  String smdgCode,
  String nmftaCode
) {
  @Builder
  public Carrier { }
}
