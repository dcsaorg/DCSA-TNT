package org.dcsa.tnt.service.domain;

import lombok.Builder;
import org.dcsa.skernel.domain.persistence.entity.enums.DimensionUnit;
import org.dcsa.tnt.persistence.entity.enums.VesselType;

import java.util.UUID;

public record Vessel(
  UUID id,
  String vesselIMONumber,
  String name,
  String flag,
  String callSignNumber,
  Carrier vesselOperatorCarrier,
  Boolean isDummy,
  Float length,
  Float width,
  VesselType type,
  DimensionUnit dimensionUnit
) {
  @Builder
  public Vessel {}
}
