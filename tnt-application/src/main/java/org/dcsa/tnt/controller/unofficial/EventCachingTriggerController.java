package org.dcsa.tnt.controller.unofficial;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Just for testing and local development.
 */
@Profile({"test", "dev"})
@Component
public class EventCachingTriggerController extends RouteBuilder {
  @Override
  public void configure() throws Exception {
    restConfiguration().component("servlet").bindingMode(RestBindingMode.json);
    rest("/unoffical")//POST does not require a body in the request as it acts as a trigger. POST was chosen since it is not safe and not idempotent
      .post("/events-cache-trigger").id("unofficial-events-cache-trigger-endpoint").to("direct:events-cache-trigger");
  }
}
