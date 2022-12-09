package org.dcsa.tnt.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.apache.camel.FluentProducerTemplate;
import org.dcsa.tnt.persistence.entity.EventCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Just for testing and local development.
 */
@Profile({"test", "dev"})
@RestController
@RequiredArgsConstructor
public class EventCachingTriggerController {

  @Autowired FluentProducerTemplate fluentProducerTemplate;

  @PostMapping(value = "/unofficial/events-cache-trigger", produces = "application/json")
  @ResponseBody
  public List<EventCache> triggerEventCacheProcessing() {
    return fluentProducerTemplate.to("direct:events-cache-trigger").request(List.class);
  }
}
