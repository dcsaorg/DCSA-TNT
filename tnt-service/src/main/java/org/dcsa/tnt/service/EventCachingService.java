package org.dcsa.tnt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.dcsa.tnt.persistence.entity.EventCache;
import org.dcsa.tnt.persistence.entity.EventCacheQueue;
import org.dcsa.tnt.persistence.repository.EventCacheRepository;
import org.dcsa.tnt.transferobjects.EventTO;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventCachingService extends RouteBuilder {
  private final AggregatedEventService aggregatedEventService;
  private final EventCacheRepository eventCacheRepository;
  private final ObjectMapper objectMapper;

  @Override
  public void configure() {
    from("{{camel.route.event-cache-queue}}")
      .bean(this, "cacheEvent")
    ;
  }

  @SneakyThrows
  public void cacheEvent(EventCacheQueue eventCacheQueue) {
    EventTO to = aggregatedEventService.findById(eventCacheQueue.getEventID());
    eventCacheRepository.save(EventCache.builder()
        .eventID(to.getEventID())
        .eventType(eventCacheQueue.getEventType())
        .eventCreatedDateTime(to.getEventCreatedDateTime())
        .content(objectMapper.writeValueAsString(to))
      .build());
  }
}
