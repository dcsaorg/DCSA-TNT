package org.dcsa.tnt.transferobjects;

import lombok.Builder;
import org.dcsa.tnt.transferobjects.enums.FacilityTypeCode;

public record TransportCallTO(
  String transportCallReference,
  String portVisitReference,
  String carrierServiceCode,
  String universalServiceReference,
  String carrierExportVoyageNumber,
  String universalExportVoyageReference,
  String carrierImportVoyageNumber,
  String universalImportVoyageReference,
  Integer transportCallSequenceNumber,
  String modeOfTransportCode,
  LocationTO location,
  FacilityTypeCode facilityTypeCode,
  VesselTO vessel
) {
  @Builder
  public TransportCallTO { }
}
