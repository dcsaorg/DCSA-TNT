package org.dcsa.tnt.service.domain;

import lombok.Builder;
import org.dcsa.skernel.domain.persistence.entity.enums.FacilityTypeCode;
import org.dcsa.skernel.domain.persistence.entity.enums.PortCallStatusCode;

import java.util.UUID;

public record TransportCall(
  UUID id,
  String transportCallReference,
  Integer transportCallSequenceNumber,
  FacilityTypeCode facilityTypeCode,
  Location location,
  String modeOfTransportCode,
  PortCallStatusCode portCallStatusCode,
  String portVisitReference,
  Vessel vessel,
  Voyage importVoyage,
  Voyage exportVoyage
) {
  @Builder
  public TransportCall { }
}
