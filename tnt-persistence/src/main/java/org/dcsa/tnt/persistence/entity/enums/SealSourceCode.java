package org.dcsa.tnt.persistence.entity.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SealSourceCode {
  CAR("Carrier"),
  SHI("Shipper"),
  PHY("Phytosanitar"),
  VET("Veterinary"),
  CUS("Customs")
  ;

  @Getter
  private String description;
}
