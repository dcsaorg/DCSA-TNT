package org.dcsa.tnt.service;

import org.dcsa.core.service.ExtendedBaseService;
import org.dcsa.tnt.model.Event;
import org.dcsa.tnt.model.Events;
import org.dcsa.tnt.model.enums.EventType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface EventService extends ExtendedBaseService<Event, UUID> {
    Mono<Events> findAllWrapped(Flux<Event> events);
    Flux<Event> findAllTypes(List<EventType> eventType, String bookingReference, String equipmentReference);
}
