package org.dcsa.tnt.util;

import lombok.extern.slf4j.Slf4j;
import org.dcsa.tnt.model.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A class calling callBackHandlers when subscriptions are activated because an event has been triggered
 */
@Slf4j
public class EventCallbackHandler extends Thread {

    private final Flux<String> callbackUrls;
    private final Event event;

    public EventCallbackHandler(Flux<String> callbackUrls, ShipmentEvent event) {
        this.callbackUrls=callbackUrls;
        this.event=event;
    }

    public EventCallbackHandler(Flux<String> callbackUrls, TransportEvent event) {
        this.callbackUrls=callbackUrls;
        this.event=event;
    }

    public EventCallbackHandler(Flux<String> callbackUrls, EquipmentEvent event) {
        this.callbackUrls=callbackUrls;
        this.event=event;
    }

    public EventCallbackHandler(Flux<String> callbackUrls, TransportEquipmentEvent event) {
        this.callbackUrls=callbackUrls;
        this.event=event;
    }

    @Override
    public void run (){
        WebClient webClient = WebClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .build();
        Events eventsWrapper = new Events(event);
        callbackUrls.parallel().runOn(Schedulers.boundedElastic()).concatMap(callbackUrl -> {
            URI uri;
            try {
                uri = new URI(callbackUrl);
            } catch (URISyntaxException e) {
                log.warn("Could not parse URI \"" + callbackUrl + "\", skipping notification. Error: "
                        + e.getLocalizedMessage());
                return Mono.empty();
            }
            return webClient.post()
                    .uri(uri)
                    .bodyValue(eventsWrapper)
                    .retrieve()
                    .toBodilessEntity()
                    .doOnSuccess(success -> {
                        log.debug("Notification sent to " + callbackUrl + ", it replied with: "
                                + success.getStatusCode().toString());
                    })
                    // The "onErrorResume" is important to ensure we do not stop sending notifications
                    // simply because there are issues with one recipient.
                    .onErrorResume(error -> {
                        log.warn("Error during notification to " + callbackUrl + " " + error.getMessage());
                        return Mono.empty();
                    });
        })
                .subscribe();
    }
}
