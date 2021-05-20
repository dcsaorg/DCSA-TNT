package org.dcsa.tnt.service;

import org.dcsa.core.service.ExtendedBaseService;
import org.dcsa.tnt.model.Event;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.tnt.model.Notification;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EventSubscriptionService extends ExtendedBaseService<EventSubscription, UUID> {
    Mono<EventSubscription> emitNotification(EventSubscription eventSubscription,
                                             Flux<? extends Notification> notifications);
}
