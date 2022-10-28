package org.dcsa.tnt.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.skernel.infrastructure.pagination.Pagination;
import org.dcsa.tnt.persistence.entity.EventSubscription_;
import org.dcsa.tnt.service.EventSubscriptionService;
import org.dcsa.tnt.transferobjects.EventSubscriptionSecretTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionWithIdTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionWithSecretTO;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
public class EventSubscriptionController {
  private final EventSubscriptionService eventSubscriptionService;

  private final List<Sort.Order> defaultSort = List.of(new Sort.Order(Sort.Direction.ASC, EventSubscription_.CREATED_DATE_TIME));

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/event-subscriptions")
  public List<EventSubscriptionWithIdTO> getSubscriptions(
    @RequestParam(value = Pagination.DCSA_PAGE_PARAM_NAME, defaultValue = "0", required = false) @Min(0)
    int page,

    @RequestParam(value = Pagination.DCSA_PAGESIZE_PARAM_NAME, defaultValue = "100", required = false) @Min(1)
    int pageSize,

    HttpServletRequest request, HttpServletResponse response
  ) {
    return Pagination
      .with(request, response, page, pageSize)
      .sortBy(defaultSort)
      .paginate(eventSubscriptionService::findAll);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/event-subscriptions/{subscriptionID}")
  public EventSubscriptionWithIdTO getSubscription(@PathVariable("subscriptionID") UUID subscriptionID) {
    return eventSubscriptionService.getSubscription(subscriptionID);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = "/event-subscriptions")
  public EventSubscriptionWithIdTO createSubscription(@Valid @RequestBody EventSubscriptionWithSecretTO eventSubscription) {
    return eventSubscriptionService.createSubscription(eventSubscription);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(path = "/event-subscriptions/{subscriptionID}")
  public void deleteSubscription(@PathVariable("subscriptionID") UUID subscriptionID) {
    eventSubscriptionService.deleteSubscription(subscriptionID);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(path = "/event-subscriptions/{subscriptionID}")
  public void updateSubscription(@PathVariable("subscriptionID") UUID subscriptionID, @Valid @RequestBody EventSubscriptionTO eventSubscription) {
    eventSubscriptionService.updateSubscription(subscriptionID, eventSubscription);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(path = "/event-subscriptions/{subscriptionID}/secret")
  public void updateSecret(@PathVariable("subscriptionID") UUID subscriptionID, @Valid @RequestBody EventSubscriptionSecretTO eventSubscriptionSecret) {
    eventSubscriptionService.updateSecret(subscriptionID, eventSubscriptionSecret);
  }
}
