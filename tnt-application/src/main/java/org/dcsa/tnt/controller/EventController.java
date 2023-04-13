package org.dcsa.tnt.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.skernel.infrastructure.http.queryparams.DCSAQueryParameterParser;
import org.dcsa.skernel.infrastructure.pagination.Pagination;
import org.dcsa.skernel.infrastructure.sorting.Sorter.SortableFields;
import org.dcsa.skernel.infrastructure.validation.EnumSubset;
import org.dcsa.skernel.infrastructure.validation.UniversalServiceReference;
import org.dcsa.tnt.domain.persistence.entity.Event_;
import org.dcsa.tnt.domain.persistence.repository.specification.EventSpecification.EventFilters;
import org.dcsa.tnt.domain.valueobjects.enums.DocumentTypeCode;
import org.dcsa.tnt.domain.valueobjects.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.domain.valueobjects.enums.EventType;
import org.dcsa.tnt.domain.valueobjects.enums.ShipmentEventTypeCode;
import org.dcsa.tnt.domain.valueobjects.enums.TransportEventTypeCode;
import org.dcsa.tnt.service.EventService;
import org.dcsa.tnt.transferobjects.EventTO;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.dcsa.skernel.infrastructure.util.EnumUtil.toEnumList;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.context-path}")
public class EventController {
  private static final String TNT_EVENT_TYPES = "EQUIPMENT,TRANSPORT,SHIPMENT";
  private static final List<EventType> TNT_EVENT_TYPE_ENUMS = Arrays.stream(TNT_EVENT_TYPES.split(","))
    .map(EventType::valueOf)
    .toList();

  private final List<Sort.Order> defaultSort = List.of(new Sort.Order(Sort.Direction.ASC, Event_.EVENT_CREATED_DATE_TIME));
  private final SortableFields sortableFields = SortableFields.of(Event_.EVENT_CREATED_DATE_TIME, Event_.EVENT_DATE_TIME);

  private final EventService eventService;
  private final DCSAQueryParameterParser queryParameterParser;

  @GetMapping(path = "/events/{eventID}")
  @ResponseStatus(HttpStatus.OK)
  public EventTO findEvent(@PathVariable("eventID") String eventID) {
    return eventService.findEvent(eventID);
  }

  @GetMapping(path = "/events")
  @ResponseStatus(HttpStatus.OK)
  public List<EventTO> findEvents(
    @RequestParam(value = "eventType", required = false)
    @EnumSubset(anyOf = TNT_EVENT_TYPES)
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

    @RequestParam(value = "carrierServiceCode", required = false) @Size(max = 11)
    String carrierServiceCode,

    @RequestParam(value = "equipmentReference", required = false) @Size(max = 15)
    String equipmentReference,

    @RequestParam(value = Pagination.DCSA_PAGE_PARAM_NAME, defaultValue = "0", required = false) @Min(0)
    int page,

    @RequestParam(value = Pagination.DCSA_PAGESIZE_PARAM_NAME, defaultValue = "100", required = false) @Min(1)
    int pageSize,

    @RequestParam(value = Pagination.DCSA_SORT_PARAM_NAME, required = false)
    String sort,

    @RequestParam
    Map<String, String> queryParams,

    HttpServletRequest request, HttpServletResponse response
  ) {
    return Pagination
      .with(request, response, page, pageSize)
      .sortBy(sort, defaultSort, sortableFields)
      .paginate(pageRequest ->
        eventService.findAll(pageRequest, EventFilters.builder()
            .eventCreatedDateTime(queryParameterParser.parseCustomQueryParameter(queryParams, "eventCreatedDateTime", OffsetDateTime::parse))
            .eventDateTime(queryParameterParser.parseCustomQueryParameter(queryParams, "eventDateTime", OffsetDateTime::parse))
            .eventType(toEnumSet(eventType, EventType.class))
            .shipmentEventTypeCode(toEnumSet(shipmentEventTypeCode, ShipmentEventTypeCode.class))
            .transportEventTypeCode(toEnumSet(transportEventTypeCode, TransportEventTypeCode.class))
            .equipmentEventTypeCode(toEnumSet(equipmentEventTypeCode, EquipmentEventTypeCode.class))
            .documentTypeCode(toEnumSet(documentTypeCode, DocumentTypeCode.class))
            .documentReference(documentReference)
            .transportCallReference(transportCallReference)
            .carrierExportVoyageNumber(carrierExportVoyageNumber)
            .universalExportVoyageReference(universalExportVoyageReference)
            .universalServiceReference(universalServiceReference)
            .UNLocationCode(UNLocationCode)
            .vesselIMONumber(vesselIMONumber)
            .carrierServiceCode(carrierServiceCode)
            .equipmentReference(equipmentReference)
            .build())
      );
  }

  private <T extends Enum<T>> Set<T> toEnumSet(String values, Class<T> enumClass) {
    return values == null ? null : Set.copyOf(toEnumList(values, enumClass));
  }
}
