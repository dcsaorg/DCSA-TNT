package org.dcsa.tnt.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.entity.EventCache_;
import org.dcsa.tnt.persistence.repository.specification.EventCacheSpecification.EventCacheFilters;
import org.dcsa.tnt.service.EventService;
import org.dcsa.tnt.service.domain.Event;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.function.Function;

/**
 * Just for testing.
 */
@Profile("test")
@RestController
@RequiredArgsConstructor
public class EventCacheDaoController {
  private final EventService eventService;

  private final List<Sort.Order> defaultSort = List.of(new Sort.Order(Sort.Direction.ASC, EventCache_.EVENT_CREATED_DATE_TIME));

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/unofficial/events-dao/")
  public List<Event> findAll(HttpServletRequest request, HttpServletResponse response) {
    return eventService.findAll(
      PageRequest.of(0, 1000, Sort.by(defaultSort)),
      EventCacheFilters.builder().build(),
      Function.identity()
      ).content();
  }
}
