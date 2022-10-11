package org.dcsa.tnt.transferobjects;

import lombok.Builder;
import org.dcsa.tnt.transferobjects.enums.EventType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EventMetadataTO(
  UUID eventID,
  OffsetDateTime eventCreatedDateTime,
  EventType eventType,
  UUID retractedEventID
) {
  @Builder
  public EventMetadataTO { }
}
