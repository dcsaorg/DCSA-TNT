package org.dcsa.tnt.transferobjects;

import lombok.Builder;
import org.dcsa.tnt.transferobjects.enums.FacilityTypeCode;
import org.dcsa.tnt.transferobjects.enums.PortCallStatusCode;

import java.util.UUID;

public record TransportCallTO(
  UUID id,

  String transportCallReference,

  Integer transportCallSequenceNumber,

  FacilityTypeCode facilityTypeCode,

  LocationTO location,

  String modeOfTransportCode,

  PortCallStatusCode portCallStatusCode,

  String portVisitReference,

  FacilityTO facility,

  VesselTO vessel,

  VoyageTO importVoyage,

  VoyageTO exportVoyage
) {
  @Builder
  public TransportCallTO { }
}
