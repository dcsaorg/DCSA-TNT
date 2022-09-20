package org.dcsa.tnt.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SealType {
  KLP("Keyless padlock"),
  BLT("Bolt"),
  WIR("Wire")
  ;

  @Getter
  private String description;
}
