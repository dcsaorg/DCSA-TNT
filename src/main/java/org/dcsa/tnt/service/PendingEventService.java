package org.dcsa.tnt.service;

import org.dcsa.core.service.ExtendedBaseService;
import org.dcsa.core.events.model.PendingMessage;

import java.util.UUID;

public interface PendingEventService extends ExtendedBaseService<PendingMessage, UUID> {
}
