package org.dcsa.tnt.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DocumentTypeCode {
  CBR("Carrier Booking Request Reference"),
  BKG("Booking"),
  SHI("Shipping Instructions"),
  TRD("Transport document"),
  DEI("Delivery Instructions"),
  DEO("Delivery Order"),
  TRO("Transport Order"),
  CRO("Container Release Order"),
  ARN("Arrival Notice"),
  VGM("Verified Gross Mass"),
  CAS("Cargo Survey"),
  CUC("Customs Clearance"),
  DGD("Dangerous Good Declaration"),
  OOG("Out Of Gauge"),
  CQU("Contract Quotation"),
  INV("Invoice")
  ;

  @Getter
  private String description;
}
