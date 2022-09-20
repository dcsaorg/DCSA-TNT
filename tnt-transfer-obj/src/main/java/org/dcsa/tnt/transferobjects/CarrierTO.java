package org.dcsa.tnt.transferobjects;

import lombok.Builder;

public record CarrierTO(
  String carrierName,
  String smdgCode,
  String nmftaCode
) {
  @Builder
  public CarrierTO { }
}
