package org.dcsa.tnt.service.unofficial;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ToDynamicDefinition;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.dcsa.tnt.persistence.entity.EventCache;
import org.dcsa.tnt.persistence.entity.EventCacheQueue;
import org.dcsa.tnt.persistence.entity.EventSubscriptionEventType;
import org.dcsa.tnt.persistence.entity.enums.EventType;
import org.dcsa.tnt.persistence.entity.enums.TransportEventTypeCode;
import org.dcsa.tnt.persistence.repository.*;
import org.dcsa.tnt.service.EventCachingService;
import org.dcsa.tnt.service.EventService;
import org.dcsa.tnt.service.domain.TransportCall;
import org.dcsa.tnt.service.domain.TransportEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@CamelSpringBootTest
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@UseAdviceWith
@ExtendWith({MockitoExtension.class})
class EventCachingTriggerServiceTest {

  @MockBean private DocumentReferenceRepository documentReferenceRepository;

  @MockBean private EventService eventService;

  @MockBean private EventCacheRepository eventCacheRepository;

  @MockBean private EventSubscriptionEventType eventSubscriptionService;

  @MockBean private EventSubscriptionRepository eventSubscriptionRepository;

  @MockBean private ReferenceRepository referenceRepository;

  @MockBean private SealRepository sealRepository;

  @MockBean private PlatformTransactionManager platformTransactionManager;

  @MockBean private EventCacheQueueDeadRepository eventCacheQueueDeadRepository;

  @InjectMocks private EventCachingService eventCachingService;

  @Autowired private ProducerTemplate producerTemplate;

  @Autowired private CamelContext camelContext;

  @EndpointInject("mock:test")
  private MockEndpoint mock;

  @Test
  void eventCachingTriggerServiceTest_testSingleItemFound() throws Exception {
    EventCacheQueue eventCacheQueue =
        EventCacheQueue.builder().eventID(UUID.randomUUID()).eventType(EventType.TRANSPORT).build();

    setupCamelRouteAndContext(List.of(eventCacheQueue));

    TransportEvent foundTransportEvent = new TransportEvent();
    foundTransportEvent.setTransportEventTypeCode(TransportEventTypeCode.ARRI);
    foundTransportEvent.setChangeRemark("changemark");
    foundTransportEvent.setTransportCall(TransportCall.builder().id(UUID.randomUUID()).build());
    when(eventService.findTransportEvent(any())).thenReturn(foundTransportEvent);
    when(eventCacheRepository.save(any())).thenReturn(EventCache.builder().build());

    mock.setExpectedCount(1);
    EventCacheQueue response =
        producerTemplate.requestBody("direct:events-cache-trigger", null, EventCacheQueue.class);

    mock.assertIsSatisfied();
    assertEquals(eventCacheQueue.getEventID(), response.getEventID());
  }

  @Test
  void eventCachingTriggerServiceTest_noItemsFound() throws Exception {
    setupCamelRouteAndContext(Collections.emptyList());

    TransportEvent foundTransportEvent = new TransportEvent();
    foundTransportEvent.setTransportEventTypeCode(TransportEventTypeCode.ARRI);
    foundTransportEvent.setChangeRemark("changemark");
    foundTransportEvent.setTransportCall(TransportCall.builder().id(UUID.randomUUID()).build());
    when(eventService.findTransportEvent(any())).thenReturn(foundTransportEvent);
    when(eventCacheRepository.save(any())).thenReturn(EventCache.builder().build());

    mock.setExpectedCount(0);
    List response =
      producerTemplate.requestBody("direct:events-cache-trigger", null, List.class);

    mock.assertIsSatisfied();
    assertEquals(0, response.size());
  }

  @Test
  void eventCachingTriggerServiceTest_testMultipleItemsFound() throws Exception {
    EventCacheQueue eventCacheQueue1 =
        EventCacheQueue.builder().eventID(UUID.randomUUID()).eventType(EventType.TRANSPORT).build();

    EventCacheQueue eventCacheQueue2 =
        EventCacheQueue.builder().eventID(UUID.randomUUID()).eventType(EventType.TRANSPORT).build();

    setupCamelRouteAndContext(List.of(eventCacheQueue1, eventCacheQueue2));

    TransportEvent foundTransportEvent = new TransportEvent();
    foundTransportEvent.setTransportEventTypeCode(TransportEventTypeCode.ARRI);
    foundTransportEvent.setChangeRemark("changemark");
    foundTransportEvent.setTransportCall(TransportCall.builder().id(UUID.randomUUID()).build());
    when(eventService.findTransportEvent(any())).thenReturn(foundTransportEvent);
    when(eventCacheRepository.save(any())).thenReturn(EventCache.builder().build());

    mock.setExpectedCount(2);
    List<EventCacheQueue> response =
        producerTemplate.requestBody("direct:events-cache-trigger", null, List.class);

    mock.assertIsSatisfied();
    assertEquals(eventCacheQueue1.getEventID(), response.get(0).getEventID());
    assertEquals(2, response.size());
  }

  private void setupCamelRouteAndContext(List<EventCacheQueue> eventCacheQueue) throws Exception {
    AdviceWith.adviceWith(
        camelContext,
        "unofficial-event-cache-trigger",
        routeBuilder -> {
          routeBuilder
              .weaveById("poll-queue")
              .replace()
              .process(exchange -> exchange.getIn().setBody(eventCacheQueue));

          routeBuilder.weaveByType(ToDynamicDefinition.class).replace().to("mock:test");
        });

    camelContext.start();
  }
}
