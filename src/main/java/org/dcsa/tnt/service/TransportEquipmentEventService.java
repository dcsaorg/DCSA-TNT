package org.dcsa.tnt.service;

import org.dcsa.core.service.ExtendedBaseService;
import org.dcsa.tnt.model.TransportEquipmentEvent;
import org.dcsa.tnt.model.enums.EventType;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface TransportEquipmentEventService extends ExtendedBaseService<TransportEquipmentEvent, UUID> {
    Flux<TransportEquipmentEvent> findTransportEquipmentEvents(List<EventType> eventType, String bookingReference, String equipmentReference);
}
