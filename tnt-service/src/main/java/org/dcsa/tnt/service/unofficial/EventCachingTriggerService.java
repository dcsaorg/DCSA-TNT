package org.dcsa.tnt.service.unofficial;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.dcsa.tnt.service.EventCachingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Just for testing and local development.
 */
@Profile({"test", "dev"})
@Service
@RequiredArgsConstructor
public class EventCachingTriggerService extends RouteBuilder {

  private final EventCachingService eventCachingService;

  //Use the Spring transaction manager as the transaction manager in Camel
  @Bean(name = "PROPAGATION_REQUIRES_NEW")
  public SpringTransactionPolicy camelTxManager(PlatformTransactionManager txManager) {
    SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
    propagationRequired.setTransactionManager(txManager);
    propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRES_NEW");
    return propagationRequired;
  }

  @Override
  public void configure() {
    from("direct:events-cache-trigger")
        .onException(Exception.class)
        .useOriginalMessage()
        .process(eventCachingService::handleFailedEventMessage)
        .continued(true) //need continued so it can execute the delete query
      .end()
      .routeId("unofficial-event-cache-trigger")
      .transacted("PROPAGATION_REQUIRES_NEW")
      .log("triggering event cache repository population")
      .pollEnrich("{{camel.route.event-cache-queue}}", 5000L).id("poll-queue")//since we are wrapping a polling action into a blocking operation we give a timeout of 5 seconds
      .split(body()) //split is needed since the pollenrich of the jpa endpoint will combine multiple records in a list.
        .log("found messages in the queue for processing, ${body.eventID}")
        .bean(eventCachingService, "cacheEvent")
        //pollenrich does not auto delete the records after the route is complete, so an explicit delete is needed
        .toD("jpa:org.dcsa.tnt.persistence.entity.EventCacheQueue?query=delete from org.dcsa.tnt.persistence.entity.EventCacheQueue e where e.eventID = '${body.eventID}'")
      .end();
  }
}
