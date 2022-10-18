package org.dcsa.tnt.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.http.queryparams.DCSAQueryParameterParser;
import org.dcsa.skernel.infrastructure.pagination.Cursor;
import org.dcsa.skernel.infrastructure.pagination.Cursor.SortBy;
import org.dcsa.skernel.infrastructure.pagination.CursorDefaults;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.dcsa.skernel.infrastructure.pagination.Paginator;
import org.dcsa.skernel.infrastructure.sorting.Sorter;
import org.dcsa.skernel.infrastructure.validation.EnumSubset;
import org.dcsa.skernel.infrastructure.validation.UniversalServiceReference;
import org.dcsa.tnt.persistence.entity.EventCache_;
import org.dcsa.tnt.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.tnt.persistence.entity.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.persistence.entity.enums.EventType;
import org.dcsa.tnt.persistence.entity.enums.ShipmentEventTypeCode;
import org.dcsa.tnt.persistence.entity.enums.TransportEventTypeCode;
import org.dcsa.tnt.persistence.repository.specification.EventCacheSpecification.EventCacheFilters;
import org.dcsa.tnt.service.EventService;
import org.dcsa.tnt.service.mapping.transferobject.EventMapper;
import org.dcsa.tnt.transferobjects.EventTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.dcsa.skernel.infrastructure.util.EnumUtil.toEnumList;


@Validated
@RestController
@RequiredArgsConstructor
public class EventController {
  private final Sorter sortHelper = new Sorter(
    List.of(new SortBy(Sort.Direction.ASC, EventCache_.EVENT_CREATED_DATE_TIME)),
    EventCache_.EVENT_CREATED_DATE_TIME, EventCache_.EVENT_DATE_TIME
  );

  private final EventService eventService;
  private final EventMapper eventMapper;
  private final Paginator paginator;
  private final DCSAQueryParameterParser queryParameterParser;

  @GetMapping(path = "/events/{eventID}")
  @ResponseStatus(HttpStatus.OK)
  public EventTO findEvent(@PathVariable("eventID") UUID eventID) {
    return eventService.findEvent(eventID, eventMapper::toDTO);
  }

  @GetMapping(path = "/events")
  @ResponseStatus(HttpStatus.OK)
  public List<EventTO> findEvents(
    @RequestParam(value = "eventType", required = false)
    @EnumSubset(anyOf = {"SHIPMENT", "TRANSPORT", "EQUIPMENT"})
    String eventType,

    @RequestParam(value = "shipmentEventTypeCode", required = false)
    String shipmentEventTypeCode,

    @RequestParam(value = "transportEventTypeCode", required = false)
    String transportEventTypeCode,

    @RequestParam(value = "equipmentEventTypeCode", required = false)
    String equipmentEventTypeCode,

    @RequestParam(value = "documentTypeCode", required = false)
    String documentTypeCode,

    @RequestParam(value = "documentReference", required = false) @Size(max = 100)
    String documentReference,

    @RequestParam(value = "transportCallReference", required = false) @Size(max = 100)
    String transportCallReference,

    @RequestParam(value = "carrierExportVoyageNumber", required = false) @Size(max = 50)
    String carrierExportVoyageNumber,

    @Pattern(regexp = "\\d{2}[0-9A-Z]{2}[NEWS]", message = "Not a valid voyage reference")
    @RequestParam(value = "universalExportVoyageReference", required = false)
    String universalExportVoyageReference,

    @UniversalServiceReference
    @RequestParam(value = "universalServiceReference", required = false)
    String universalServiceReference,

    @RequestParam(value = "UNLocationCode", required = false) @Size(max = 5)
    String UNLocationCode,

    @RequestParam(value = "vesselIMONumber", required = false) @Size(max = 7)
    String vesselIMONumber,

    @RequestParam(value = "carrierServiceCode", required = false) @Size(max = 5)
    String carrierServiceCode,

    @RequestParam(value = "equipmentReference", required = false) @Size(max = 15)
    String equipmentReference,

    @RequestParam(value = "sort", required = false)
    String sort,

    @RequestParam(value = "limit", defaultValue = "100", required = false) @Min(1)
    int limit,

    @RequestParam
    Map<String, String> queryParams,

    HttpServletRequest request, HttpServletResponse response
  ) {
    Cursor cursor = paginator.parseRequest(request, new CursorDefaults(limit, sortHelper.parseSort(sort)));

    PagedResult<EventTO> result = eventService.findAll(cursor, EventCacheFilters.builder()
        .eventCreatedDateTime(queryParameterParser.parseCustomQueryParameter(queryParams, "eventCreatedDateTime", OffsetDateTime::parse))
        .eventDateTime(queryParameterParser.parseCustomQueryParameter(queryParams, "eventDateTime", OffsetDateTime::parse))
        .eventType(toEnumList(eventType, EventType.class))
        .shipmentEventTypeCode(toEnumList(shipmentEventTypeCode, ShipmentEventTypeCode.class))
        .transportEventTypeCode(toEnumList(transportEventTypeCode, TransportEventTypeCode.class))
        .equipmentEventTypeCode(toEnumList(equipmentEventTypeCode, EquipmentEventTypeCode.class))
        .documentTypeCode(toEnumList(documentTypeCode, DocumentTypeCode.class))
        .documentReference(documentReference)
        .transportCallReference(transportCallReference)
        .carrierExportVoyageNumber(carrierExportVoyageNumber)
        .universalExportVoyageReference(universalExportVoyageReference)
        .universalServiceReference(universalServiceReference)
        .UNLocationCode(UNLocationCode)
        .vesselIMONumber(vesselIMONumber)
        .carrierServiceCode(carrierServiceCode)
        .equipmentReference(equipmentReference)
      .build(),
      eventMapper::toDTO);

    paginator.setPageHeaders(request, response, cursor, result);
    return result.content();
  }
}
