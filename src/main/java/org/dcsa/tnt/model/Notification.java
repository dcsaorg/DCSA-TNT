package org.dcsa.tnt.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface Notification {

    UUID getEventID();
    OffsetDateTime getEventCreatedDateTime();
}
