package org.dcsa.tnt.transferobjects;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UnmappedEventTO (
  UUID eventID,
  OffsetDateTime enqueuedAtDateTime
) {
  @Builder
  public UnmappedEventTO {}
}
