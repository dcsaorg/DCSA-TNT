package org.dcsa.tnt.domain.valueobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DocumentReferenceType {
  BKG("Booking"),
  SHI("Shipping Instructions"),
  TRD("Transport document"),
  CBR("Carrier Booking Request Reference");

  @Getter private String description;
}
