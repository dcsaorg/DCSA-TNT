package org.dcsa.tnt.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DimensionUnit {
  MTR("Meter"),
  FOT("Foot")
  ;

  @Getter
  private final String name;
}
