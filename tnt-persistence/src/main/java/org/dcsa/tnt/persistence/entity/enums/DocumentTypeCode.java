package org.dcsa.tnt.persistence.entity.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DocumentTypeCode {
  BKG("Booking"),
  SHI("Shipping Instructions"),
  VGM("Verified Gross Mass"),
  SRM("Shipment Release Message"),
  TRD("Transport document"),
  ARN("Arrival Notice"),
  CAS("Cargo Survey"),
  CUS("Customs Inspection"),
  DGD("Dangerous Good Declaration"),
  OOG("Out Of Gauge"),
  CBR("Carrier Booking Request Reference")
  ;

  @Getter
  private String description;
}
