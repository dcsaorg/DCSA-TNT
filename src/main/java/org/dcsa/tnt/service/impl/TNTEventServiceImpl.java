package org.dcsa.tnt.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dcsa.core.events.model.EquipmentEvent;
import org.dcsa.core.events.model.Event;
import org.dcsa.core.events.model.ShipmentEvent;
import org.dcsa.core.events.model.TransportEvent;
import org.dcsa.core.events.repository.EventRepository;
import org.dcsa.core.events.repository.PendingEventRepository;
import org.dcsa.core.events.service.*;
import org.dcsa.core.events.service.impl.GenericEventServiceImpl;
import org.dcsa.core.exception.NotFoundException;
import org.dcsa.core.extendedrequest.ExtendedRequest;
import org.dcsa.tnt.service.TNTEventService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
public class TNTEventServiceImpl extends GenericEventServiceImpl implements TNTEventService {

  public TNTEventServiceImpl(
      TransportEventService transportEventService,
      EquipmentEventService equipmentEventService,
      ShipmentEventService shipmentEventService,
      OperationsEventService operationsEventService,
      EventRepository eventRepository,
      PendingEventRepository pendingEventRepository) {
    super(
        shipmentEventService,
        transportEventService,
        equipmentEventService,
        operationsEventService,
        eventRepository,
        pendingEventRepository);
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
                    return shipmentEventService.loadRelatedEntities((ShipmentEvent) event);
                default:
                    return Mono.empty();
            }
        });
    }

    @Override
    public Mono<Event> findById(UUID id) {
        return Mono.<Event>empty()
                .switchIfEmpty(getTransportEventRelatedEntities(id))
                .switchIfEmpty(getShipmentEventRelatedEntities(id))
                .switchIfEmpty(getEquipmentEventRelatedEntities(id))
                .switchIfEmpty(Mono.error(new NotFoundException("No event was found with id: " + id)));
    }

    @Override
    public Mono<Event> create(Event event) {
        if (event.getEventCreatedDateTime() == null) {
            event.setEventCreatedDateTime(OffsetDateTime.now());
        }
        return super.create(event)
                .flatMap(savedEvent -> pendingEventRepository.enqueueUnmappedEventID(event.getEventID())
                        .thenReturn(savedEvent)
                );
    }
}
