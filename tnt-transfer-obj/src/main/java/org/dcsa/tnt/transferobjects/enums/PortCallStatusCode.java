package org.dcsa.tnt.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PortCallStatusCode {
  OMIT("Omit"),
  BLNK("Blank"),
  ADHO("Ad Hoc"),
  PHOT("Phase Out"),
  PHIN("Phase In"),
  ROTC("Rotation Change"),
  SLID("Sliding");

  @Getter
  private final String name;
}
