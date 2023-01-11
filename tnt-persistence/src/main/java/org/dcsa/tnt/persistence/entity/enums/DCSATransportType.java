package org.dcsa.tnt.persistence.entity.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DCSATransportType {
  VESSEL(1, "Maritime transport"),
  RAIL(2, "Rail transport"),
  TRUCK(3, "Road transport"),
  BARGE(8, "Inland water Transport")
  ;

  @Getter
  private final Integer code;

  @Getter
  private final String name;
}
