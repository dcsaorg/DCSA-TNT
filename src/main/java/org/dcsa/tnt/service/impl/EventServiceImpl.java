package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.core.events.model.EquipmentEvent;
import org.dcsa.core.events.model.Event;
import org.dcsa.core.events.service.impl.GenericEventServiceImpl;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.tnt.repository.EventSubscriptionRepository;
import org.dcsa.tnt.service.EventService;
import org.dcsa.tnt.service.EventSubscriptionService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl extends GenericEventServiceImpl implements EventService {

    private final EventSubscriptionRepository eventSubscriptionRepository;
    private final EventSubscriptionService eventSubscriptionService;
    private final ThreadPoolTaskExecutor executor;
    private final ReactiveTransactionManager transactionManager;

    @Override
    public Mono<Event> create(Event event) {
        String equipmentReference;
        if (event instanceof EquipmentEvent) {
            equipmentReference = ((EquipmentEvent) event).getEquipmentReference();
        } else {
            equipmentReference = null;
        }
        Supplier<Flux<EventSubscription>> subscriptionFlux = () -> eventSubscriptionRepository.findSubscriptionsByFilters(event.getEventType(), equipmentReference);
        if (event.getEventCreatedDateTime() == null) {
            event.setEventCreatedDateTime(OffsetDateTime.now());
        }
        return super.create(event)
                .doOnNext(e -> executor.submit(ProcessEvents.of(transactionManager, eventSubscriptionService, subscriptionFlux, e)));
    }

    @Slf4j
    @RequiredArgsConstructor(staticName = "of")
    private static class ProcessEvents implements Runnable {
        private final ReactiveTransactionManager transactionManager;
        private final EventSubscriptionService eventSubscriptionService;
        private final Supplier<Flux<EventSubscription>> eventSubscriptionSupplier;
        private final Event notification;

        public void run() {
            List<Event> notifications = Collections.singletonList(notification);
            TransactionalOperator transactionalOperator = TransactionalOperator.create(transactionManager);
            try {
                eventSubscriptionSupplier.get().concatMap(eventSubscriptionState ->
                        // We want to commit each subscription independently to limit the consequences of a
                        transactionalOperator.transactional(eventSubscriptionService.emitNotification(eventSubscriptionState, Flux.fromIterable(notifications)))
                )
                .count()
                .block();
            } catch (Throwable e) {
                // The default exception handler for uncaught exceptions is to silently discard
                // them.  We "improve" the situation by at least logging them but there is no
                // point in re-throwing it (except for errors as one should never make an error
                // "handled").
                log.warn("Event submission failed", e);
                if (e instanceof Error) {
                    throw (Error)e;
                }
            }
        }
    }
}
