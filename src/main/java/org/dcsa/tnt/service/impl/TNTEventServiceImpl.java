package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.events.model.EquipmentEvent;
import org.dcsa.core.events.model.Event;
import org.dcsa.core.events.model.ShipmentEvent;
import org.dcsa.core.events.model.TransportEvent;
import org.dcsa.core.events.repository.EventRepository;
import org.dcsa.core.events.repository.PendingEventRepository;
import org.dcsa.core.events.service.EquipmentEventService;
import org.dcsa.core.events.service.GenericEventService;
import org.dcsa.core.events.service.ShipmentEventService;
import org.dcsa.core.events.service.TransportEventService;
import org.dcsa.core.events.service.impl.GenericEventServiceImpl;
import org.dcsa.core.exception.NotFoundException;
import org.dcsa.core.extendedrequest.ExtendedRequest;
import org.dcsa.core.service.impl.ExtendedBaseServiceImpl;
import org.dcsa.tnt.service.TNTEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TNTEventServiceImpl extends GenericEventServiceImpl implements TNTEventService {

    @Autowired
    private PendingEventRepository pendingEventRepository;

    @Override
    public Class<Event> getModelClass() {
        return Event.class;
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
