package org.dcsa.tnt.util;

import lombok.extern.slf4j.Slf4j;
import org.dcsa.tnt.model.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import static io.restassured.RestAssured.given;

/**
 * A class calling callBackHandlers when subscriptions are activated because an event has been triggered
 */
@Slf4j
public class EventCallbackHandler extends Thread {

    Flux<String> callbackUrls;
    Event event;

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

@Override
    public void run (){
        callbackUrls.parallel().runOn(Schedulers.elastic()).doOnNext(callbackUrl -> {
            try {
                given()
                        .contentType("application/json")
                        .body(event)
                        .post(callbackUrl);
            } catch (Exception e) {
                log.warn("Failed to connect to "+callbackUrl + " " + e.getMessage());
            }
        }).subscribe();
    }
}
