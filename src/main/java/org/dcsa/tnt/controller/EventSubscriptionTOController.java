package org.dcsa.tnt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dcsa.core.controller.BaseController;
import org.dcsa.core.exception.CreateException;
import org.dcsa.core.exception.DeleteException;
import org.dcsa.core.exception.GetException;
import org.dcsa.core.exception.UpdateException;
import org.dcsa.core.extendedrequest.ExtendedParameters;
import org.dcsa.core.extendedrequest.ExtendedRequest;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.tnt.model.transferobjects.EventSubscriptionTO;
import org.dcsa.tnt.service.EventSubscriptionTOService;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "event-subscriptions", produces = {MediaType.APPLICATION_JSON_VALUE})
@Tag(name = "Event Subscriptions", description = "The event subscription API")
public class EventSubscriptionTOController extends BaseController<EventSubscriptionTOService, EventSubscriptionTO, UUID> {

    private final ExtendedParameters extendedParameters;
    private final R2dbcDialect r2dbcDialect;
    private final EventSubscriptionTOService eventSubscriptionTOService;

    @Override
    public EventSubscriptionTOService getService() {
        return eventSubscriptionTOService;
    }

    @Override
    public String getType() {
        return "EventSubscription";
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<EventSubscriptionTO> findById(@PathVariable UUID id) {
        return eventSubscriptionTOService.findById(id);
    }

    @Operation(
            summary = "Find all Event Subscriptions",
            description = "Finds all Event Subscriptions in the database",
            tags = { "Event Subscriptions" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventSubscription.class))))
    })
    @GetMapping
    public Flux<EventSubscriptionTO> findAll(ServerHttpResponse response, ServerHttpRequest request) {
        ExtendedRequest<EventSubscription> extendedRequest = new ExtendedRequest<>(extendedParameters, r2dbcDialect, EventSubscription.class);
        try {
            extendedRequest.parseParameter(request.getQueryParams());
        } catch (GetException getException) {
            return Flux.error(getException);
        }

        return getService().findAllExtended(extendedRequest).doOnComplete(
                () -> {
                    // Add Link headers to the response
                    extendedRequest.insertHeaders(response, request);
                }
        );
    }

    @Operation(
            summary = "Find all Event Subscriptions",
            description = "Finds all Event Subscription in the database",
            tags = { "Event Subscriptions" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventSubscription.class))))
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EventSubscriptionTO> create(@Valid @RequestBody EventSubscriptionTO eventSubscriptionTO) {
        if (eventSubscriptionTO.getId() != null) {
            return Mono.error(new CreateException("Id not allowed when creating a new " + getType()));
        }
        return eventSubscriptionTOService.create(eventSubscriptionTO);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<EventSubscriptionTO> update(@PathVariable UUID id, @Valid @RequestBody EventSubscriptionTO eventSubscriptionTO) {
        if (!Objects.equals(id, eventSubscriptionTO.getId())) {
            return Mono.error(new UpdateException("Id in url does not match id in body"));
        }
        return eventSubscriptionTOService.update(eventSubscriptionTO);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@RequestBody EventSubscriptionTO eventSubscriptionTO) {
        if (eventSubscriptionTO.getId() == null) {
            return Mono.error(new DeleteException("No Id provided in " + getType() + " object"));
        }
        return eventSubscriptionTOService.delete(eventSubscriptionTO);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable UUID id) {
        return eventSubscriptionTOService.deleteById(id);
    }

}
