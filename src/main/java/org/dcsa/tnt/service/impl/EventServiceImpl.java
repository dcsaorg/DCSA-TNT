package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.exception.NotFoundException;
import org.dcsa.core.service.impl.ExtendedBaseServiceImpl;
import org.dcsa.tnt.model.*;
import org.dcsa.tnt.repository.EventRepository;
import org.dcsa.tnt.repository.EventSubscriptionRepository;
import org.dcsa.tnt.service.EventService;
import org.dcsa.tnt.util.EventCallbackHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventServiceImpl extends ExtendedBaseServiceImpl<EventRepository, Event, UUID> implements EventService {
    private final ShipmentEventServiceImpl shipmentEventService;
    private final TransportEventServiceImpl transportEventService;
    private final TransportEquipmentEventServiceImpl transportEquipmentEventService;
    private final EquipmentEventServiceImpl equipmentEventService;
    private final EventSubscriptionRepository eventSubscriptionRepository;
    private final EventRepository eventRepository;


    @Override
    public EventRepository getRepository() {
        return eventRepository;
    }

    @Override
    public Mono<Event> findById(UUID id) {
        return eventRepository.findById(UUID.randomUUID())
                .switchIfEmpty(transportEventService.findById(id))
                .switchIfEmpty(shipmentEventService.findById(id))
                .switchIfEmpty(transportEquipmentEventService.findById(id))
                .switchIfEmpty(equipmentEventService.findById(id))
                .switchIfEmpty(Mono.error(new NotFoundException("No event was found with id: " + id)));
    }

    @Override
    public Mono<Event> save(Event event) {
        switch (event.getEventType()) {
            case SHIPMENT:
                return shipmentEventService.create((ShipmentEvent) event).doOnNext(
                        e -> new EventCallbackHandler(
                                eventSubscriptionRepository.findSubscriptionsByFilters(e.getEventType(),
                                        null), e)
                                .start()
                ).cast(Event.class);
            case TRANSPORT:
                return transportEventService.create((TransportEvent) event).doOnNext(
                        e -> new EventCallbackHandler(
                                eventSubscriptionRepository.findSubscriptionsByFilters(e.getEventType(),
                                        null), e)
                                .start()
                ).cast(Event.class);
            case TRANSPORTEQUIPMENT:
                return transportEquipmentEventService.create((TransportEquipmentEvent) event).doOnNext(
                        e -> new EventCallbackHandler(
                                eventSubscriptionRepository.findSubscriptionsByFilters(e.getEventType(),
                                        e.getEquipmentReference()), e)
                                .start()
                ).cast(Event.class);
            case EQUIPMENT:
                return equipmentEventService.create((EquipmentEvent) event).doOnNext(
                        e -> new EventCallbackHandler(
                                eventSubscriptionRepository.findSubscriptionsByFilters(e.getEventType(),
                                e.getEquipmentReference()), e)
                                .start()
                ).cast(Event.class);
            default:
                return Mono.error(new IllegalStateException("Unexpected value: " + event.getEventType()));
        }
    }
}
