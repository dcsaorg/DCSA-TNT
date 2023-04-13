package org.dcsa.tnt.domain.valueobjects.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum VesselType {
  GECA("General cargo"),
  CONT("Container"),
  RORO("RoRo"),
  CARC("Car carrier"),
  PASS("Passenger"),
  FERY("Ferry"),
  BULK("Bulk"),
  TANK("Tanker"),
  LPGT("Liquified gaz tanker"),
  ASSI("Assistance"),
  PLOT("Pilot boat");

  @Getter private final String description;
}
