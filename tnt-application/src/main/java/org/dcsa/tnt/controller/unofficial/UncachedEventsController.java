package org.dcsa.tnt.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.service.EventService;
import org.dcsa.tnt.service.mapping.transferobject.EventMapper;
import org.dcsa.tnt.transferobjects.EventTO;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Just for testing.
 */
@Profile("test")
@RestController
@RequiredArgsConstructor
public class UncachedEventsController {
  private final EventService eventService;
  private final EventMapper eventMapper;

  @GetMapping(path = "/unofficial/uncached-shipment-events")
  @ResponseStatus(HttpStatus.OK)
  public List<EventTO> findAllShipmentEvents() {
    return eventService.findAllShipmentEvents(eventMapper::toDTO);
  }

  @GetMapping(path = "/unofficial/uncached-transport-events")
  @ResponseStatus(HttpStatus.OK)
  public List<EventTO> findAllTransportEvents() {
    return eventService.findAllTransportEvents(eventMapper::toDTO);
  }

  @GetMapping(path = "/unofficial/uncached-equipment-events")
  @ResponseStatus(HttpStatus.OK)
  public List<EventTO> findAllEquipmentEvents() {
    return eventService.findAllEquipmentEvents(eventMapper::toDTO);
  }
}
