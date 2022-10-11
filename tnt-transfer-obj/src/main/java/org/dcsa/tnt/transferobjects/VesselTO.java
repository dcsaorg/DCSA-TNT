package org.dcsa.tnt.transferobjects;

import lombok.Builder;
import org.dcsa.tnt.transferobjects.enums.DimensionUnit;
import org.dcsa.tnt.transferobjects.enums.VesselType;

import java.util.UUID;

public record VesselTO(
  String vesselIMONumber,
  String name,
  String flag,
  String callSign,
  String operatorCarrierCode, // <-
  String operatorCarrierCodeListProvider // <-
  /*
  CarrierTO vesselOperatorCarrier,
  Boolean isDummy,
  Float length,
  Float width,
  VesselType type,
  DimensionUnit dimensionUnit
   */
) {
  @Builder
  public VesselTO {}
}
