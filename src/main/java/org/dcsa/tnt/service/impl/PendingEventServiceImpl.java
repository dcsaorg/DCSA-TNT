package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.core.events.model.PendingMessage;
import org.dcsa.core.events.repository.PendingEventRepository;
import org.dcsa.core.events.service.impl.MessageSignatureHandler;
import org.dcsa.core.service.impl.ExtendedBaseServiceImpl;
import org.dcsa.tnt.service.EventService;
import org.dcsa.tnt.service.EventSubscriptionService;
import org.dcsa.tnt.service.PendingEventService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class PendingEventServiceImpl extends ExtendedBaseServiceImpl<PendingEventRepository, PendingMessage, UUID> implements PendingEventService {

    private final PendingEventRepository pendingEventRepository;
    private final ReactiveTransactionManager transactionManager;
    private final EventService eventService;
    private final EventSubscriptionService eventSubscriptionService;
    private final MessageSignatureHandler messageSignatureHandler;

    private Disposable processUnmappedEvent;
    private Disposable processPendingEventQueue;

    @Override
    public PendingEventRepository getRepository() {
        return pendingEventRepository;
    }

    @Scheduled(
            cron = "${dcsa.pendingEventService.backgroundTasks.processUnmappedEventQueue.cronSchedule:45 */1 * * * *}"
    )
    public synchronized void processUnmappedEventQueue() {
        if (processUnmappedEvent != null && !processUnmappedEvent.isDisposed()) {
            log.info("Skipping processUnmappedEventQueue task. Previous job is still on-going");
            return;
        }
        processUnmappedEvent = null;
        Instant start = Instant.now();
        TransactionalOperator transactionalOperator = TransactionalOperator.create(transactionManager);
        log.info("Starting processUnmappedEventQueue task");

        Mono<Void> mapJob = pendingEventRepository.pollUnmappedEventID()
                .checkpoint("Fetched unmappedEvent event")
                .flatMap(eventService::findById)
                .flatMap(mappedEvent ->
                        Mono.zip(
                                Mono.just(mappedEvent.getEventID()),
                                eventSubscriptionService.findSubscriptionsFor(mappedEvent)
                                        .map(eventSubscription -> {
                                            PendingMessage pendingMessage = new PendingMessage();
                                            pendingMessage.setEventID(mappedEvent.getEventID());
                                            pendingMessage.setSubscriptionID(eventSubscription.getSubscriptionID());
                                            return pendingMessage;
                                        }).concatMap(this::create)
                                        .count()
                )).doOnSuccess(tuple -> {
                    Instant finish = Instant.now();
                    Duration duration = Duration.between(start, finish);
                    if (tuple != null) {
                        UUID eventID = tuple.getT1();
                        long count = tuple.getT2();
                        if (count > 0) {
                            log.info("Successfully generated " + count + " pending event(s) for event "
                                    + eventID + ". The processUnmappedEventQueue job took " + duration);
                        } else {
                            log.info("No subscribers for event " + eventID + ". The processUnmappedEventQueue job took "
                                    + duration);
                        }
                    } else {
                        log.info("No events to send. The processUnmappedEventQueue job took " + duration);
                    }
                })
                .then();
        processUnmappedEvent = transactionalOperator.transactional(mapJob).doOnTerminate(() -> {
            if (processUnmappedEvent != null) {
                processUnmappedEvent.dispose();
            }
            processUnmappedEvent = null;
        }).subscribe();
    }

    @Scheduled(
            cron = "${dcsa.pendingEventService.backgroundTasks.processPendingEventQueue.cronSchedule:15 */1 * * * *}"
    )
    public synchronized void processPendingEventQueue() {
        if (processUnmappedEvent != null && !processUnmappedEvent.isDisposed()) {
            log.info("Skipping processPendingEventQueue task. Previous job is still on-going");
            return;
        }
        processUnmappedEvent = null;
        Instant start = Instant.now();
        TransactionalOperator transactionalOperator = TransactionalOperator.create(transactionManager);
        log.info("Starting processPendingEventQueue task");

        Mono<Void> mapJob = pendingEventRepository.pollPendingEvent()
                .checkpoint("Fetched pending event")
                .flatMap(pendingMessage ->
                        eventSubscriptionService.findById(pendingMessage.getSubscriptionID())
                            .flatMap(eventSubscription ->
                                messageSignatureHandler.emitMessage(eventSubscription, Flux.just(pendingMessage))
                            )
                ).flatMap(submissionResult -> {
                    if (submissionResult.isSuccessful()) {
                        return eventSubscriptionService.update(submissionResult.getEventSubscription())
                                .thenReturn(submissionResult);
                    }
                    return eventSubscriptionService.update(submissionResult.getEventSubscription())
                            .thenMany(Flux.fromIterable(submissionResult.getPendingMessages()))
                            .concatMap(pendingEventRepository::insert)
                            .then(Mono.just(submissionResult));
                }).doOnSuccess(submissionResult -> {
                    Instant finish = Instant.now();
                    Duration duration = Duration.between(start, finish);
                    if (submissionResult == null) {
                        log.info("No pending messages that can be send at the moment. The processPendingEventQueue job took "
                                + duration);
                    } else if (submissionResult.isSuccessful()) {
                        log.info("Successfully submitted " + submissionResult.getPendingMessages().size()
                                + " pending message(s). The processPendingEventQueue job took " + duration);
                        for (PendingMessage pendingMessage : submissionResult.getPendingMessages()) {
                            log.info("Submitted " + pendingMessage.getEventID() + " to subscription "
                                    + submissionResult.getEventSubscription().getSubscriptionID());
                        }
                    } else {
                        log.info("Delivery of pending messages to " + submissionResult.getEventSubscription().getSubscriptionID()
                                + " failed (will retry later). The processPendingEventQueue job took "
                                + duration);
                    }
                }).then();

        processPendingEventQueue = transactionalOperator.transactional(mapJob).doOnTerminate(() -> {
            if (processPendingEventQueue != null) {
                processPendingEventQueue.dispose();
            }
            processPendingEventQueue = null;
        }).subscribe();
    }

}
