package org.dcsa.tnt.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.tnt.persistence.entity.EventSubscription;
import org.dcsa.tnt.persistence.repository.EventSubscriptionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Just for testing.
 */
@RestController
@RequiredArgsConstructor
public class EventSubscriptionDaoController {
  private final EventSubscriptionRepository eventSubscriptionRepository;

  @Profile("test")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/unofficial/event-subscriptions-dao/{subscriptionID}")
  public EventSubscription getSubscription(@PathVariable("subscriptionID") UUID subscriptionID) {
    return eventSubscriptionRepository.findById(subscriptionID)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No event-subscription found with id = " + subscriptionID));
  }
}
