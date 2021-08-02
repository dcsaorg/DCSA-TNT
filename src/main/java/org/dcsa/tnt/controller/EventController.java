package org.dcsa.tnt.controller;

import org.dcsa.core.events.controller.AbstractEventController;
import org.dcsa.core.events.model.Event;
import org.dcsa.core.events.model.enums.EquipmentEventTypeCode;
import org.dcsa.core.events.model.enums.ShipmentEventTypeCode;
import org.dcsa.core.events.model.enums.TransportDocumentTypeCode;
import org.dcsa.core.events.model.enums.TransportEventTypeCode;
import org.dcsa.core.events.util.ExtendedGenericEventRequest;
import org.dcsa.core.extendedrequest.ExtendedRequest;
import org.dcsa.core.validator.EnumSubset;
import org.dcsa.core.validator.ValidEnum;
import org.dcsa.tnt.service.TNTEventService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@RestController
@Validated
@RequestMapping(value = "events", produces = {MediaType.APPLICATION_JSON_VALUE})
public class EventController extends AbstractEventController<TNTEventService, Event> {

    private final TNTEventService tntEventService;

    public EventController(@Qualifier("TNTEventServiceImpl") TNTEventService tntEventService) {
        this.tntEventService = tntEventService;
    }

    @Override
    public TNTEventService getService() {
        return tntEventService;
    }

    @Override
    public String getType() {
        return "Event";
    }

    @Override
    protected ExtendedRequest<Event> newExtendedRequest() {
        return new ExtendedGenericEventRequest(extendedParameters, r2dbcDialect);
    }

  @GetMapping
  public Flux<Event> findAll(
      @RequestParam(value = "eventType", required = false)
          @EnumSubset(anyOf = {"SHIPMENT", "TRANSPORT", "EQUIPMENT"})
          String eventType,
      @RequestParam(value = "shipmentEventTypeCode", required = false)
          @ValidEnum(clazz = ShipmentEventTypeCode.class)
          String shipmentEventTypeCode,
      @RequestParam(value = "carrierBookingReference", required = false) @Size(max = 35)
          String carrierBookingReference,
      @RequestParam(value = "transportDocumentReference", required = false) @Size(max = 20)
          String transportDocumentReference,
      @RequestParam(value = "transportDocumentTypeCode", required = false)
          @ValidEnum(clazz = TransportDocumentTypeCode.class)
          String transportDocumentTypeCode,
      @RequestParam(value = "transportEventTypeCode", required = false)
          @ValidEnum(clazz = TransportEventTypeCode.class)
          String transportEventTypeCode,
      @RequestParam(value = "transportCallID", required = false) @Size(max = 100)
          String transportCallID,
      @RequestParam(value = "vesselIMONumber", required = false) @Size(max = 7)
          String vesselIMONumber,
      @RequestParam(value = "carrierVoyageNumber", required = false) @Size(max = 50)
          String carrierVoyageNumber,
      @RequestParam(value = "carrierServiceCode", required = false) @Size(max = 5)
          String carrierServiceCode,
      @RequestParam(value = "equipmentEventTypeCode", required = false)
          @ValidEnum(clazz = EquipmentEventTypeCode.class)
          String equipmentEventTypeCode,
      @RequestParam(value = "equipmentReference", required = false) @Size(max = 15)
          String equipmentReference,
      @RequestParam(value = "limit", defaultValue = "1", required = false) @Min(1) int limit,
      ServerHttpResponse response,
      ServerHttpRequest request) {
    return super.findAll(response, request);
  }

    @Override
    public Mono<Event> create(@Valid @RequestBody Event event) {
        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN));
    }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid param value")
  @ExceptionHandler(ConstraintViolationException.class)
  public void badRequest() {}
}