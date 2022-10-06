package org.dcsa.tnt.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.skernel.infrastructure.pagination.Cursor;
import org.dcsa.skernel.infrastructure.pagination.CursorDefaults;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.dcsa.skernel.infrastructure.pagination.Paginator;
import org.dcsa.tnt.persistence.entity.EventCache_;
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
  private final Paginator paginator;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/event-subscriptions")
  public List<EventSubscriptionWithIdTO> getSubscriptions(
    @RequestParam(value = "limit", defaultValue = "100", required = false) @Min(1)
    int limit,
    HttpServletRequest request, HttpServletResponse response
  ) {
    Cursor cursor = paginator.parseRequest(
      request,
      new CursorDefaults(limit, Sort.Direction.ASC, EventSubscription_.CREATED_DATE_TIME)
    );

    PagedResult<EventSubscriptionWithIdTO> result = eventSubscriptionService.findAll(cursor);

    paginator.setPageHeaders(request, response, cursor, result);
    return result.content();
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
