package org.dcsa.tnt.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.events.controller.AbstractEventController;
import org.dcsa.core.events.model.Event;
import org.dcsa.core.events.util.ExtendedGenericEventRequest;
import org.dcsa.core.extendedrequest.ExtendedRequest;
import org.dcsa.tnt.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "events", produces = {MediaType.APPLICATION_JSON_VALUE})
public class EventController extends AbstractEventController<EventService, Event> {

    private final EventService eventService;

    @Override
    public EventService getService() {
        return eventService;
    }

    @Override
    protected ExtendedRequest<Event> newExtendedRequest() {
        return new ExtendedGenericEventRequest(extendedParameters, r2dbcDialect);
    }

    @Override
    public Mono<Event> create(@Valid @RequestBody Event event) {
        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN));
    }
}
