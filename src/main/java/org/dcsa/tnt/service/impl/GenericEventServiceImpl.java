package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.events.model.EquipmentEvent;
import org.dcsa.core.events.model.Event;
import org.dcsa.core.events.model.ShipmentEvent;
import org.dcsa.core.events.model.TransportEvent;
import org.dcsa.core.events.repository.EventRepository;
import org.dcsa.core.events.service.EquipmentEventService;
import org.dcsa.core.events.service.GenericEventService;
import org.dcsa.core.events.service.ShipmentEventService;
import org.dcsa.core.events.service.TransportEventService;
import org.dcsa.core.exception.NotFoundException;
import org.dcsa.core.extendedrequest.ExtendedRequest;
import org.dcsa.core.service.impl.ExtendedBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GenericEventServiceImpl extends ExtendedBaseServiceImpl<EventRepository, Event, UUID> implements GenericEventService {

    @Autowired
    private ShipmentEventService shipmentEventService;

    @Autowired
    private TransportEventService transportEventService;

    @Autowired
    private EquipmentEventService equipmentEventService;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public EventRepository getRepository() {
        return eventRepository;
    }

    @Override
    public Flux<Event> findAllExtended(ExtendedRequest<Event> extendedRequest) {
        return super.findAllExtended(extendedRequest).concatMap(event -> {
            switch (event.getEventType()) {
                case TRANSPORT:
                    return transportEventService.loadRelatedEntities((TransportEvent) event);
                case EQUIPMENT:
                    return equipmentEventService.loadRelatedEntities((EquipmentEvent) event);
                case SHIPMENT:
                    return shipmentEventService.loadRelatedEntities((ShipmentEvent)event);
                default:
                    return Mono.just(event);
            }
        });
    }

    @Override
    public Mono<Event> findById(UUID id) {
        return Mono.<Event>empty()
                .switchIfEmpty(transportEventService.findById(id).cast(Event.class))
                .switchIfEmpty(shipmentEventService.findById(id).cast(Event.class))
                .switchIfEmpty(equipmentEventService.findById(id).cast(Event.class))
                .switchIfEmpty(Mono.error(new NotFoundException("No event was found with id: " + id)));
    }

    @Override
    public Mono<Event> create(Event event) {
        switch (event.getEventType()) {
            case SHIPMENT:
                return shipmentEventService.create((ShipmentEvent) event).cast(Event.class);
            case TRANSPORT:
                return transportEventService.create((TransportEvent) event).cast(Event.class);
            case EQUIPMENT:
                return equipmentEventService.create((EquipmentEvent) event).cast(Event.class);
            default:
                return Mono.error(new IllegalStateException("Unexpected value: " + event.getEventType()));
        }
    }
}
