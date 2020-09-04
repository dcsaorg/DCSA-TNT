package org.dcsa.tnt.service;

import org.dcsa.core.service.ExtendedBaseService;
import org.dcsa.tnt.model.TransportEvent;
import org.dcsa.tnt.model.enums.EventType;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface TransportEventService extends ExtendedBaseService<TransportEvent, UUID> {
    Flux<TransportEvent> findTransportEvents(List<EventType> eventType, String bookingReference, String equipmentReference);
}
