package org.dcsa.tnt.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.service.EventService;
import org.dcsa.tnt.transferobjects.EquipmentEventTO;
import org.dcsa.tnt.transferobjects.ShipmentEventTO;
import org.dcsa.tnt.transferobjects.TransportEventTO;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Just for testing.
 */
@RestController
@RequiredArgsConstructor
public class UncachedEventsController {
  private final EventService eventService;

  @Profile("test")
  @GetMapping(path = "/unofficial/uncached-shipment-events")
  @ResponseStatus(HttpStatus.OK)
  public List<ShipmentEventTO> findAllShipmentEvents() {
    return eventService.findAllShipmentEvents();
  }

  @Profile("test")
  @GetMapping(path = "/unofficial/uncached-transport-events")
  @ResponseStatus(HttpStatus.OK)
  public List<TransportEventTO> findAllTransportEvents() {
    return eventService.findAllTransportEvents();
  }

  @Profile("test")
  @GetMapping(path = "/unofficial/uncached-equipment-events")
  @ResponseStatus(HttpStatus.OK)
  public List<EquipmentEventTO> findAllEquipmentEvents() {
    return eventService.findAllEquipmentEvents();
  }
}
