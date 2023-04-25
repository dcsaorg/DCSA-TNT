package org.dcsa.tnt.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.service.unofficial.UnofficialEventService;
import org.dcsa.tnt.transferobjects.EventTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.context-path}/unofficial")
public class UnofficialEventController {
  private final UnofficialEventService eventService;

  @PostMapping(path = "/events/")
  @ResponseStatus(HttpStatus.CREATED)
  public void createEvent(@RequestBody List<EventTO> eventTOs) {
    eventTOs.forEach(eventService::saveEvent);
  }
}
