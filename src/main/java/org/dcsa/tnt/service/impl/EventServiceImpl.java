package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.core.exception.NotFoundException;
import org.dcsa.core.service.impl.ExtendedBaseServiceImpl;
import org.dcsa.tnt.model.*;
import org.dcsa.tnt.repository.EventRepository;
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
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl extends ExtendedBaseServiceImpl<EventRepository, Event, UUID> implements EventService {
    private final ShipmentEventServiceImpl shipmentEventService;
    private final TransportEventServiceImpl transportEventService;
    private final EquipmentEventServiceImpl equipmentEventService;
    private final EventSubscriptionRepository eventSubscriptionRepository;
    private final EventRepository eventRepository;
    private final EventSubscriptionService eventSubscriptionService;
    private final ThreadPoolTaskExecutor executor;
    private final ReactiveTransactionManager transactionManager;


    @Override
    public EventRepository getRepository() {
        return eventRepository;
    }

    @Override
    public Mono<Event> findById(UUID id) {
        return eventRepository.findById(UUID.randomUUID())
                .switchIfEmpty(transportEventService.findById(id))
                .switchIfEmpty(shipmentEventService.findById(id))
                .switchIfEmpty(equipmentEventService.findById(id))
                .switchIfEmpty(Mono.error(new NotFoundException("No event was found with id: " + id)));
    }

    @Override
    public Mono<Event> create(Event event) {
        String equipmentReference;
        if (event instanceof EquipmentEvent) {
            equipmentReference = ((EquipmentEvent) event).getEquipmentReference();
        } else {
            equipmentReference = null;
        }
        Supplier<Flux<EventSubscription>> subscriptionFlux = () -> eventSubscriptionRepository.findSubscriptionsByFilters(event.getEventType(), equipmentReference);
        Mono<Event> createEventMono;
        if (event.getEventCreatedDateTime() == null) {
            event.setEventCreatedDateTime(OffsetDateTime.now());
        }
        switch (event.getEventType()) {
            case SHIPMENT:
                assert event instanceof ShipmentEvent;
                createEventMono = shipmentEventService.create((ShipmentEvent) event).cast(Event.class);
                break;
            case TRANSPORT:
                assert event instanceof TransportEvent;
                createEventMono = transportEventService.create((TransportEvent) event).cast(Event.class);
                break;
            case EQUIPMENT:
                assert event instanceof EquipmentEvent;
                createEventMono = equipmentEventService.create((EquipmentEvent) event).cast(Event.class);
                break;
            default:
                return Mono.error(new IllegalStateException("Unexpected value: " + event.getEventType()));
        }
        return createEventMono
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
