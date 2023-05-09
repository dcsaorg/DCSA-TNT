package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.dcsa.tnt.domain.persistence.entity.Event;
import org.dcsa.tnt.domain.persistence.repository.EventRepository;
import org.dcsa.tnt.domain.persistence.repository.specification.EventSpecification;
import org.dcsa.tnt.domain.persistence.repository.specification.EventSpecification.EventFilters;
import org.dcsa.tnt.service.mapping.EventMapper;
import org.dcsa.tnt.transferobjects.EventTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class EventService {
  private final EventRepository eventRepository;
  private final EventMapper eventMapper;

  @Transactional
  public EventTO findEvent(String eventId) {
    return eventRepository.findById(eventId)
      .map(Event::getContent)
      .map(eventMapper::toDTO)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No event found with id = " + eventId));
  }

  @Transactional
  public PagedResult<EventTO> findAll(final PageRequest pageRequest, final EventFilters filters) {
    return new PagedResult<>(
        eventRepository.findAll(EventSpecification.withFilters(filters), pageRequest),
        event -> eventMapper.toDTO(event.getContent()));
  }
}
