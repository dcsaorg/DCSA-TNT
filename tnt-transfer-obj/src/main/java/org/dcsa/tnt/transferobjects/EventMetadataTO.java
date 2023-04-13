package org.dcsa.tnt.transferobjects;

import lombok.Builder;
import org.dcsa.tnt.transferobjects.enums.EventType;

import java.time.OffsetDateTime;

public record EventMetadataTO(
  String eventID,
  OffsetDateTime eventCreatedDateTime,
  EventType eventType,
  String retractedEventID
) {
  @Builder(toBuilder = true)
  public EventMetadataTO { }
}
