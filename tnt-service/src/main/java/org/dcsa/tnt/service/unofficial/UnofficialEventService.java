package org.dcsa.tnt.service.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.tnt.domain.persistence.entity.Event;
import org.dcsa.tnt.domain.persistence.entity.EventDocumentReference;
import org.dcsa.tnt.domain.persistence.entity.EventReference;
import org.dcsa.tnt.domain.persistence.repository.EventDocumentReferenceRepository;
import org.dcsa.tnt.domain.persistence.repository.EventReferenceRepository;
import org.dcsa.tnt.domain.persistence.repository.EventRepository;
import org.dcsa.tnt.domain.valueobjects.DocumentReference;
import org.dcsa.tnt.domain.valueobjects.DomainEvent;
import org.dcsa.tnt.domain.valueobjects.Reference;
import org.dcsa.tnt.transferobjects.EventMetadataTO;
import org.dcsa.tnt.transferobjects.EventPayloadTO;
import org.dcsa.tnt.transferobjects.EventTO;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnofficialEventService {
  private final EventRepository eventRepository;
  private final EventReferenceRepository referenceRepository;
  private final EventDocumentReferenceRepository documentReferenceRepository;
  private final UnofficialEventMapper eventMapper;

  @Transactional
  public void saveEvent(EventTO eventTO) {
    if (eventTO.metadata().eventID() != null && eventRepository.findById(eventTO.metadata().eventID().toString()).isPresent()) {
      // This is unofficial so just ignore if the event already exist
      return;
    }

    EventMetadataTO metadata = eventTO.metadata();
    EventPayloadTO payload = eventTO.payload();

    if (metadata.retractedEventID() == null && payload == null) {
      throw ConcreteRequestErrorMessageException.invalidInput("event should either have a retractedEventID or a payload");
    }
    if (metadata.retractedEventID() != null && payload != null) {
      throw ConcreteRequestErrorMessageException.invalidInput("event should not have both a retractedEventID or a payload");
    }

    EventTO updated = eventTO.toBuilder()
      .metadata(metadata.toBuilder()
        .eventID(Objects.requireNonNullElseGet(metadata.eventID(), () -> UUID.randomUUID().toString()))
        .eventCreatedDateTime(Objects.requireNonNullElseGet(metadata.eventCreatedDateTime(), OffsetDateTime::now))
        .build())
      .build();

    DomainEvent domainEvent = eventMapper.toDomain(updated);
    eventRepository.save(toEntity(domainEvent));

    List<DocumentReference> relatedDocumentReferences = checkList(domainEvent.getRelatedDocumentReferences());
    List<Reference> references = checkList(domainEvent.getReferences());

    if (relatedDocumentReferences != null || references != null) {
      eventRepository.flush();
      if (relatedDocumentReferences != null) {
        documentReferenceRepository.saveAll(
          relatedDocumentReferences.stream()
            .map(ref -> EventDocumentReference.builder()
              .eventId(domainEvent.getEventID().toString())
              .type(ref.type())
              .value(ref.value())
              .build())
            .toList()
        );
      }
      if (references != null) {
        referenceRepository.saveAll(
          references.stream()
            .map(ref -> EventReference.builder()
              .eventId(domainEvent.getEventID().toString())
              .type(ref.type())
              .value(ref.value())
              .build())
            .toList()
        );
      }
    }
  }

  private Event toEntity(DomainEvent domainEvent) {
    return Event.builder()
      .eventId(domainEvent.getEventID().toString())
      .eventType(domainEvent.getEventType().name())
      .content(domainEvent)
      .eventCreatedDateTime(domainEvent.getEventCreatedDateTime())
      .eventDateTime(domainEvent.getEventDateTime())
      .build();
  }

  private <T> List<T> checkList(List<T> list) {
    return list == null || list.isEmpty() ? null : list;
  }
}
