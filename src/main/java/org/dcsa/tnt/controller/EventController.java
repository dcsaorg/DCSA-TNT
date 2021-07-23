package org.dcsa.tnt.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.controller.ExtendedBaseController;
import org.dcsa.core.events.model.Event;
import org.dcsa.core.events.service.GenericEventService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "events", produces = {MediaType.APPLICATION_JSON_VALUE})
public class EventController extends ExtendedBaseController<GenericEventService, Event, UUID> {

    private final GenericEventService genericEventService;

    @Override
    public GenericEventService getService() {
        return genericEventService;
    }

    @Override
    public String getType() {
        return "Event";
    }

}