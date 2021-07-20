package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.core.events.model.Event;
import org.dcsa.core.events.repository.PendingEventRepository;
import org.dcsa.core.events.service.impl.GenericEventServiceImpl;
import org.dcsa.tnt.service.EventService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl extends GenericEventServiceImpl implements EventService {

    private final PendingEventRepository pendingEventRepository;

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
