package org.dcsa.tnt.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.pagination.Cursor;
import org.dcsa.skernel.infrastructure.pagination.CursorDefaults;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.dcsa.skernel.infrastructure.pagination.Paginator;
import org.dcsa.skernel.infrastructure.validation.EnumSubset;
import org.dcsa.tnt.persistence.entity.EventCache_;
import org.dcsa.tnt.persistence.entity.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.persistence.entity.enums.EventType;
import org.dcsa.tnt.persistence.entity.enums.ShipmentEventTypeCode;
import org.dcsa.tnt.persistence.entity.enums.TransportDocumentTypeCode;
import org.dcsa.tnt.persistence.entity.enums.TransportEventTypeCode;
import org.dcsa.tnt.persistence.repository.specification.EventCacheSpecification.EventCacheFilters;
import org.dcsa.tnt.service.EventService;
import org.dcsa.tnt.transferobjects.EventTO;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class EventController {
  private final EventService eventService;
  private final Paginator paginator;

  @GetMapping(path = "/events")
  @ResponseStatus(HttpStatus.OK)
  public List<EventTO> findEvents(
    @RequestParam(value = "eventType", required = false)
    @EnumSubset(anyOf = {"SHIPMENT", "TRANSPORT", "EQUIPMENT"})
    String eventType,

    @RequestParam(value = "shipmentEventTypeCode", required = false)
    ShipmentEventTypeCode shipmentEventTypeCode,

    @RequestParam(value = "carrierBookingReference", required = false) @Size(max = 35)
    String carrierBookingReference,

    @RequestParam(value = "transportDocumentReference", required = false) @Size(max = 20)
    String transportDocumentReference,

    @RequestParam(value = "transportDocumentTypeCode", required = false)
    TransportDocumentTypeCode transportDocumentTypeCode,

    @RequestParam(value = "transportEventTypeCode", required = false)
    TransportEventTypeCode transportEventTypeCode,

    @RequestParam(value = "transportCallID", required = false) @Size(max = 100)
    String transportCallID,

    @RequestParam(value = "vesselIMONumber", required = false) @Size(max = 7)
    String vesselIMONumber,

    @RequestParam(value = "carrierVoyageNumber", required = false) @Size(max = 50)
    String carrierVoyageNumber,

    @RequestParam(value = "carrierServiceCode", required = false) @Size(max = 5)
    String carrierServiceCode,

    @RequestParam(value = "equipmentEventTypeCode", required = false)
    EquipmentEventTypeCode equipmentEventTypeCode,

    @RequestParam(value = "equipmentReference", required = false) @Size(max = 15)
    String equipmentReference,

    @RequestParam(value = "limit", defaultValue = "100", required = false) @Min(1)
    int limit,

    @RequestParam(value = "API-Version", required = false)
    String apiVersion,

    HttpServletRequest request, HttpServletResponse response
  ) {
    Cursor cursor = paginator.parseRequest(
      request,
      new CursorDefaults(limit, Sort.Direction.ASC, EventCache_.EVENT_CREATED_DATE_TIME)
    );

    PagedResult<EventTO> result = eventService.findAll(cursor, EventCacheFilters.builder()
        .eventType(toEnumList(eventType, EventType.class))
        .shipmentEventTypeCode(shipmentEventTypeCode)
        .carrierBookingReference(carrierBookingReference)
        .transportDocumentReference(transportDocumentReference)
        .transportDocumentTypeCode(transportDocumentTypeCode)
        .transportEventTypeCode(transportEventTypeCode)
        .transportCallID(transportCallID)
        .vesselIMONumber(vesselIMONumber)
        .carrierVoyageNumber(carrierVoyageNumber)
        .carrierServiceCode(carrierServiceCode)
        .equipmentEventTypeCode(equipmentEventTypeCode)
        .equipmentReference(equipmentReference)
      .build());

    paginator.setPageHeaders(request, response, cursor, result);
    return result.content();
  }

  /**
   * Splits a comma-separated string and converts each subpart into
   * an enum.
   */
  private <T extends Enum<T>> List<T> toEnumList(String values, Class<T> enumClass) {
    if (values == null) {
      return null;
    }
    T[] enumValues = enumClass.getEnumConstants();
    return Arrays.stream(values.split(","))
      .map(s -> Arrays.stream(enumValues)
        .filter(v -> v.name().equals(s)).findFirst()
        .orElseThrow(() -> ConcreteRequestErrorMessageException.invalidInput("'" + s + "' cannot be mapped to " + enumClass.getSimpleName()))
      )
      .toList();
  }
}
