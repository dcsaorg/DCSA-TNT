package org.dcsa.tnt.persistence.entity.enums;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum EventType {
  SHIPMENT(false),
  TRANSPORT(true),
  EQUIPMENT(true),
  ;

  public static final Set<EventType> ALL_EVENT_TYPES_WITH_TRANSPORT_CALL = Set.copyOf(
    Arrays.stream(EventType.values())
      .filter(e -> e.hasTransportCall)
      .collect(Collectors.toSet())
  );

  private final boolean hasTransportCall;
}
