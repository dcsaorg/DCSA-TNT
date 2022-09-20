package org.dcsa.tnt.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.service.AggregatedEventService;
import org.dcsa.tnt.transferobjects.EventTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {
  private final AggregatedEventService aggregatedEventService;

  @GetMapping(path = "/events")
  @ResponseStatus(HttpStatus.OK)
  public List<EventTO> findAllEvents() {
    return aggregatedEventService.findAll();
  }
}
