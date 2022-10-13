package org.dcsa.tnt.transferobjects;

import lombok.Builder;
import org.dcsa.tnt.transferobjects.enums.DimensionUnit;
import org.dcsa.tnt.transferobjects.enums.OperatorCarrierCodeListProvider;
import org.dcsa.tnt.transferobjects.enums.VesselType;

import java.util.UUID;

public record VesselTO(
  String vesselIMONumber,
  String name,
  String flag,
  String callSign,
  String operatorCarrierCode,
  OperatorCarrierCodeListProvider operatorCarrierCodeListProvider
) {
  @Builder
  public VesselTO {}
}
