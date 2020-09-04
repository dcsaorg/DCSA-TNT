package org.dcsa.tnt.service;

import org.dcsa.core.service.ExtendedBaseService;
import org.dcsa.tnt.model.EquipmentEvent;
import org.dcsa.tnt.model.enums.EventType;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface EquipmentEventService extends ExtendedBaseService<EquipmentEvent, UUID> {
    Flux<EquipmentEvent> findEquipmentEvents(List<EventType> eventType, String bookingReference, String equipmentReference);
}
