package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.service.impl.ExtendedBaseServiceImpl;
import org.dcsa.tnt.model.EquipmentEvent;
import org.dcsa.tnt.model.enums.EventType;
import org.dcsa.tnt.repository.EquipmentEventRepository;
import org.dcsa.tnt.service.EquipmentEventService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EquipmentEventServiceImpl extends ExtendedBaseServiceImpl<EquipmentEventRepository, EquipmentEvent, UUID> implements EquipmentEventService {
    private final EquipmentEventRepository equipmentEventRepository;

    @Override
    public EquipmentEventRepository getRepository() {
        return equipmentEventRepository;
    }

    @Override
    public Class<EquipmentEvent> getModelClass() {
        return EquipmentEvent.class;
    }

    //Overriding base method here, as it marks empty results as an error, meaning we can't use switchOnEmpty()
    @Override
    public Mono<EquipmentEvent> findById(UUID id) {
        return getRepository().findById(id);
    }

    public Flux<EquipmentEvent> findEquipmentEvents(List<EventType> eventType, String bookingReference, String equipmentReference) {
        // Return empty if EQUIPMENT event type is not defined
        if (!eventType.contains(EventType.EQUIPMENT)) return Flux.empty();
        //If bookingReference is defined, we return empty - since bookingReferences don't exist in equipmentEvents
        if (bookingReference!=null ) return Flux.empty();
        return equipmentEventRepository.findAllEquipmentEventsByFilters(EventType.EQUIPMENT, null, equipmentReference);
    }
}
