package org.dcsa.tnt.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.skernel.infrastructure.pagination.Pagination;
import org.dcsa.skernel.infrastructure.sorting.Sorter.SortableFields;
import org.dcsa.tnt.domain.persistence.entity.EventSubscription_;
import org.dcsa.tnt.service.EventSubscriptionService;
import org.dcsa.tnt.transferobjects.EventSubscriptionSecretTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionWithIdTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionWithSecretTO;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.context-path}")
public class EventSubscriptionController {
  private final EventSubscriptionService eventSubscriptionService;

  private final List<Sort.Order> defaultSort = List.of(new Sort.Order(Sort.Direction.ASC, EventSubscription_.CREATED_DATE_TIME));
  private final SortableFields sortableFields = SortableFields.of(EventSubscription_.CREATED_DATE_TIME);

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/event-subscriptions")
  public List<EventSubscriptionWithIdTO> getSubscriptions(
    @RequestParam(value = Pagination.DCSA_PAGE_PARAM_NAME, defaultValue = "0", required = false) @Min(0)
    int page,

    @RequestParam(value = Pagination.DCSA_PAGESIZE_PARAM_NAME, defaultValue = "100", required = false) @Min(1)
    int pageSize,

    @RequestParam(value = Pagination.DCSA_SORT_PARAM_NAME, required = false)
    String sort,

    HttpServletRequest request, HttpServletResponse response
  ) {
    return Pagination
      .with(request, response, page, pageSize)
      .sortBy(sort, defaultSort, sortableFields)
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
