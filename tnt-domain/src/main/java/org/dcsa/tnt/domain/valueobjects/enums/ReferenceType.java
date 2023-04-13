package org.dcsa.tnt.domain.valueobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReferenceType {
  FF("Reference assigned to the shipment by the freight forwarder."),
  SI("Reference assigned to the shipment by the shipper."),
  PO("The PO reference that the shipper or freight forwarder received from the consignee and then shared with the carrier."),
  CR("Reference assigned to the shipment by the customer."),
  AAO("Reference assigned to the shipment by the consignee."),
  ECR("Unique identifier to enable release of the container from a carrier nominated depot"),
  CSI("Unique Shipment ID for the booking in the Shipper or Forwarder system. Used to identify the booking along with the Booking party."),
  BPR("A unique identifier provided by a booking party in the booking request."),
  BID("The associated booking request ID provided by the shipper."),
  EQ("Reference to the equipment that is associated with document."),
  RUC("Type of declaration field : RUC / format : NAANNNNNNNNNXXXXXXXXXXXXXXXXXXXXXX (in spanish 'Registro Único del Contribuyente'/ Tax ID number for any natural or legal person (such as a company) in Peru; ex : 2BR16404287200000000000000000434384)"),
  DUE("Type of declaration field : DU-E / format : YYBRSSSSSSSSS (from the Portuguese; “Declaração Única de Exportação”; ex : 22BR000652483)"),
  CER("Export Proof of Report Number issued by Canada customs will be a mandatory requirement for cargo to load on vessels departing from Canadian load ports. This requirement is in line with CBSA’s transition from Canadian Automated Export Declaration System (CAED) to Canadian Export Reporting System (CERS). (mandatory for Canada only; no specified format)."),
  AES("It’s the system U.S. exporters use to electronically declare their international exports (mandatory for US only; no specified format")
  ;

  @Getter
  private String description;
}
