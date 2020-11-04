package org.dcsa.tnt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dcsa.core.controller.ExtendedBaseController;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.tnt.service.EventSubscriptionService;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "event-subscriptions", produces = {MediaType.APPLICATION_JSON_VALUE})
@Tag(name = "Event Subscriptions", description = "The event subscription API")
public class EventSubscriptionController extends ExtendedBaseController<EventSubscriptionService, EventSubscription, UUID> {

    private final EventSubscriptionService eventSubscriptionService;

    @Override
    public EventSubscriptionService getService() {
        return eventSubscriptionService;
    }

    @Operation(summary = "Find all Events", description = "Finds all Events in the database", tags = { "Events" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventSubscription.class))))
    })
    @GetMapping
    public Flux<EventSubscription> findAll(ServerHttpResponse response, ServerHttpRequest request) {
        return super.findAll(response, request);
    }

    @Operation(summary = "Find all Events", description = "Finds all Events in the database", tags = { "Events" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventSubscription.class))))
    })
    @PostMapping( consumes = "application/json", produces = "application/json")
    public Mono<EventSubscription> create(@RequestBody EventSubscription eventSubscription) {
        return super.create(eventSubscription);
    }
}
