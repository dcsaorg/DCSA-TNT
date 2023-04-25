package org.dcsa.tnt.domain.valueobjects.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventClassifierCode {
  ACT("Actual"),
  PLN("Planned"),
  EST("Estimated"),
  REQ("Requested");

  private final String value;
}
