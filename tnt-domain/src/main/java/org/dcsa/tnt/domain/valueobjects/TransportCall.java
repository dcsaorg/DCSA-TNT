package org.dcsa.tnt.domain.valueobjects;

import lombok.Builder;
import org.dcsa.skernel.domain.persistence.entity.enums.FacilityTypeCode;
import org.dcsa.skernel.domain.persistence.entity.enums.PortCallStatusCode;

public record TransportCall(
  String transportCallReference,
  Integer transportCallSequenceNumber,
  FacilityTypeCode facilityTypeCode,
  Location location,
  String modeOfTransport,
  PortCallStatusCode portCallStatusCode,
  String portVisitReference,
  Vessel vessel,
  String carrierServiceCode,
  String universalServiceReference,
  String carrierExportVoyageNumber,
  String universalExportVoyageReference,
  String carrierImportVoyageNumber,
  String universalImportVoyageReference
) {
  @Builder
  public TransportCall { }
}
