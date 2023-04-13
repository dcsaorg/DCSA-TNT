package org.dcsa.tnt.domain.valueobjects;

import lombok.Builder;
import org.dcsa.tnt.domain.valueobjects.enums.OperatorCarrierCodeListProvider;

public record Vessel(
  String vesselIMONumber,
  String name,
  String flag,
  String callSign,
  String operatorCarrierCode,
  OperatorCarrierCodeListProvider operatorCarrierCodeListProvider
) {
  @Builder
  public Vessel {}
}
