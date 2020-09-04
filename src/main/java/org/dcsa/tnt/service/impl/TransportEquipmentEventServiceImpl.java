package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.service.impl.ExtendedBaseServiceImpl;
import org.dcsa.tnt.model.TransportEquipmentEvent;
import org.dcsa.tnt.model.enums.EventType;
import org.dcsa.tnt.repository.TransportEquipmentEventRepository;
import org.dcsa.tnt.service.TransportEquipmentEventService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TransportEquipmentEventServiceImpl extends ExtendedBaseServiceImpl<TransportEquipmentEventRepository, TransportEquipmentEvent, UUID> implements TransportEquipmentEventService {
    private final TransportEquipmentEventRepository transportEquipmentEventRepository;

    @Override
    public TransportEquipmentEventRepository getRepository() {
        return transportEquipmentEventRepository;
    }

    @Override
    public Class<TransportEquipmentEvent> getModelClass() {
        return TransportEquipmentEvent.class;
    }

    //Overriding base method here, as it marks empty results as an error, meaning we can't use switchOnEmpty()
    @Override
    public Mono<TransportEquipmentEvent> findById(UUID id) {
        return getRepository().findById(id);
    }

    public Flux<TransportEquipmentEvent> findTransportEquipmentEvents(List<EventType> eventType, String bookingReference, String equipmentReference) {
        // Return empty if TRANSPORTEQUIPMENT event type is not defined
        if (!eventType.contains(EventType.TRANSPORTEQUIPMENT)) return Flux.empty();
        // If bookingReference is defined, we return empty - since bookingReferences don't exist in equipmentEvents
        if (bookingReference!=null ) return Flux.empty();
        return transportEquipmentEventRepository.findTransportEquipmentEventsByFilters(EventType.TRANSPORTEQUIPMENT, null, equipmentReference);
    }
}
