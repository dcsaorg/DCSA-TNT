package org.dcsa.tnt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.pagination.Cursor;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.dcsa.tnt.persistence.entity.EquipmentEvent;
import org.dcsa.tnt.persistence.entity.EventCache;
import org.dcsa.tnt.persistence.entity.ShipmentEvent;
import org.dcsa.tnt.persistence.entity.TransportEvent;
import org.dcsa.tnt.persistence.repository.EquipmentEventRepository;
import org.dcsa.tnt.persistence.repository.EventCacheRepository;
import org.dcsa.tnt.persistence.repository.ShipmentEventRepository;
import org.dcsa.tnt.persistence.repository.TransportEventRepository;
import org.dcsa.tnt.persistence.repository.specification.EventCacheSpecification.EventCacheFilters;
import org.dcsa.tnt.service.domain.Event;
import org.dcsa.tnt.service.mapping.domain.DomainEventMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.dcsa.tnt.persistence.repository.specification.EventCacheSpecification.withFilters;

@Service
@RequiredArgsConstructor
public class EventService {
  private final EventCacheRepository eventCacheRepository;
  private final EquipmentEventRepository equipmentEventRepository;
  private final TransportEventRepository transportEventRepository;
  private final ShipmentEventRepository shipmentEventRepository;

  private final DomainEventMapper domainEventMapper;
  private final ObjectMapper objectMapper;

  private final ReferenceService referenceService;
  private final DocumentReferenceService documentReferenceService;
  private final SealService sealService;

  @Transactional
  public <T> T findEvent(UUID eventId, Function<Event, T> toMapper) {
    return eventCacheRepository.findById(eventId)
      .map(this::deserializeEvent)
      .map(toMapper)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No event found with id = " + eventId));
  }

  @Transactional
  public <T> PagedResult<T> findAll(final Cursor cursor, final EventCacheFilters filters, Function<Event, T> toMapper) {
    return new PagedResult<>(
        eventCacheRepository.findAll(withFilters(filters), cursor.toPageRequest()),
        event -> toMapper.apply(deserializeEvent(event)));
  }

  @SneakyThrows
  private Event deserializeEvent(EventCache event) {
    return objectMapper.readValue(event.getContent(), Event.class);
  }

  @Transactional
  public <T> List<T> findAllShipmentEvents(Function<Event, T> toMapper) {
    return shipmentEventRepository.findAll().stream()
      .map(this::toDomain)
      .map(toMapper)
      .toList();
  }

  @Transactional
  public <T> List<T> findAllEquipmentEvents(Function<Event, T> toMapper) {
    return equipmentEventRepository.findAll().stream()
      .map(this::toDomain)
      .map(toMapper)
      .toList();
  }

  @Transactional
  public <T> List<T> findAllTransportEvents(Function<Event, T> toMapper) {
    return transportEventRepository.findAll().stream()
      .map(this::toDomain)
      .map(toMapper)
      .toList();
  }

  public org.dcsa.tnt.service.domain.EquipmentEvent findEquipmentEvent(UUID eventId) {
    return equipmentEventRepository.findById(eventId)
      .map(this::toDomain)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No EquipmentEvent with id = " + eventId));
  }

  public org.dcsa.tnt.service.domain.ShipmentEvent findShipmentEvent(UUID eventId) {
    return shipmentEventRepository.findById(eventId)
      .map(this::toDomain)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No ShipmentEvent with id = " + eventId));
  }

  public org.dcsa.tnt.service.domain.TransportEvent findTransportEvent(UUID eventId) {
    return transportEventRepository.findById(eventId)
      .map(this::toDomain)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No TransportEvent with id = " + eventId));
  }

  private org.dcsa.tnt.service.domain.EquipmentEvent toDomain(EquipmentEvent event) {
    org.dcsa.tnt.service.domain.EquipmentEvent equipmentEvent = domainEventMapper.toDomain(event);
    equipmentEvent.setRelatedDocumentReferences(documentReferenceService.findFor(event));
    equipmentEvent.setReferences(referenceService.findFor(event));
    equipmentEvent.setSeals(sealService.findFor(event));
    return equipmentEvent;
  }

  private org.dcsa.tnt.service.domain.ShipmentEvent toDomain(ShipmentEvent event) {
    org.dcsa.tnt.service.domain.ShipmentEvent shipmentEvent = domainEventMapper.toDomain(event);
    shipmentEvent.setRelatedDocumentReferences(documentReferenceService.findFor(event));
    shipmentEvent.setReferences(referenceService.findFor(event));
    return shipmentEvent;
  }

  private org.dcsa.tnt.service.domain.TransportEvent toDomain(TransportEvent event) {
    org.dcsa.tnt.service.domain.TransportEvent transportEvent = domainEventMapper.toDomain(event);
    transportEvent.setRelatedDocumentReferences(documentReferenceService.findFor(event));
    transportEvent.setReferences(referenceService.findFor(event));
    return transportEvent;
  }
}
