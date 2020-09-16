package org.dcsa.tnt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dcsa.core.controller.BaseController;
import org.dcsa.core.exception.GetException;
import org.dcsa.core.extendedrequest.ExtendedParameters;
import org.dcsa.tnt.model.*;
import org.dcsa.tnt.service.EventService;
import org.dcsa.tnt.util.ExtendedEventRequest;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "events", produces = {MediaType.APPLICATION_JSON_VALUE})
@Tag(name = "Events", description = "The event API")
public class EventController extends BaseController<EventService, Event, UUID> {

    private final EventService eventService;

    private final ExtendedParameters extendedParameters;

    @Override
    public String getType() {
        return getService().getModelClass().getSimpleName();
    }

    @Override
    public EventService getService() {
        return eventService;
    }

    @Operation(summary = "Find all Events", description = "Finds all Events in the database", tags = { "Events" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Event.class))))
    })
    @GetMapping
    public Mono<Events> findAll(ServerHttpResponse response, ServerHttpRequest request) {
        ExtendedEventRequest extendedEventRequest = new ExtendedEventRequest(extendedParameters,
                new Class[] {EquipmentEvent.class, ShipmentEvent.class, TransportEvent.class, TransportEquipmentEvent.class});
        try {
            Map<String,String> params = request.getQueryParams().toSingleValueMap();
            extendedEventRequest.parseParameter(params);
        } catch (GetException getException) {
            return Mono.error(getException);
        }

        return getService().findAllExtended(extendedEventRequest)
                .collectList()
                .map(Events::new)
                .doOnSuccess(
                        eventWrapper -> extendedEventRequest.insertHeaders(response, request)
                );
    }

    @Operation(summary = "Find Event by ID", description = "Returns a single Event", tags = { "Event" }, parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description="Id of the Event to be obtained. Cannot be empty.", required=true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @GetMapping(value="{id}", produces = "application/json")
    @Override
    public Mono<Event> findById(@PathVariable UUID id) {
        return super.findById(id);
    }

    @Operation(summary = "Save any type of event", description = "Saves any type of event", tags = { "Events" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Override
    public Mono<Event> save(@RequestBody Event event) {
        return super.save(event);
    }

}
