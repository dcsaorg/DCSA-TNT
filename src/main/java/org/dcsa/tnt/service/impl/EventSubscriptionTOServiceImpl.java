package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.events.model.enums.EventType;
import org.dcsa.core.exception.UpdateException;
import org.dcsa.core.extendedrequest.ExtendedRequest;
import org.dcsa.core.service.impl.BaseServiceImpl;
import org.dcsa.core.util.MappingUtils;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.tnt.model.base.AbstractEventSubscription;
import org.dcsa.tnt.model.transferobjects.EventSubscriptionSecretUpdateTO;
import org.dcsa.tnt.model.transferobjects.EventSubscriptionTO;
import org.dcsa.tnt.repository.EventSubscriptionRepository;
import org.dcsa.tnt.service.EventSubscriptionService;
import org.dcsa.tnt.service.EventSubscriptionTOService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventSubscriptionTOServiceImpl extends BaseServiceImpl<EventSubscriptionTO, UUID> implements EventSubscriptionTOService {

    private static final List<EventType> ALL_EVENT_TYPES = List.of(EventType.values());

    private final EventSubscriptionService eventSubscriptionService;
    private final EventSubscriptionRepository eventSubscriptionRepository;

    @Override
    public Flux<EventSubscriptionTO> findAll() {
        return mapManyD2TO(eventSubscriptionService.findAll());
    }

    @Override
    public Mono<EventSubscriptionTO> findById(UUID id) {
        return mapSingleD2TO(eventSubscriptionService.findById(id));
    }

    @Override
    public Mono<EventSubscriptionTO> create(EventSubscriptionTO eventSubscriptionTO) {
        return eventSubscriptionService.create(MappingUtils.instanceFrom(eventSubscriptionTO, EventSubscription::new, AbstractEventSubscription.class))
                .flatMap(eventSubscription -> {
                    eventSubscriptionTO.setSubscriptionID(eventSubscription.getSubscriptionID());
                    return createEventTypes(eventSubscriptionTO);
                });
    }

    @Override
    public Mono<EventSubscriptionTO> update(EventSubscriptionTO eventSubscriptionTO) {
        if (eventSubscriptionTO.getSecret() != null) {
            return Mono.error(new UpdateException("Please omit the \"secret\" attribute.  If you want to change the"
                    + " secret, please use the dedicated secret endpoint"
                    + " (\"PUT .../event-subscriptions/" + eventSubscriptionTO.getSubscriptionID() + "/secret\")."));
        }
        return eventSubscriptionRepository.deleteEventTypesForSubscription(eventSubscriptionTO.getSubscriptionID())
                .thenReturn(eventSubscriptionTO)
                .map(subscriptionTO -> MappingUtils.instanceFrom(subscriptionTO, EventSubscription::new, AbstractEventSubscription.class))
                .flatMap(updated ->
                        eventSubscriptionService.findById(updated.getSubscriptionID())
                                .map(original -> {
                                    updated.setSecret(original.getSecret());
                                    updated.copyInternalFieldsFrom(original);

                                    // If the callbackUrl has not changed, meaning it is null, we use the original url.
                                    if (updated.getCallbackUrl() == null) {
                                        updated.setCallbackUrl(original.getCallbackUrl());
                                        eventSubscriptionTO.setCallbackUrl(updated.getCallbackUrl());
                                    }
                                    return updated;
                                })
                )
                .flatMap(eventSubscriptionService::update)
                .flatMap(ignored -> createEventTypes(eventSubscriptionTO));
    }

    public Mono<Void> updateSecret(UUID subscriptionID, EventSubscriptionSecretUpdateTO eventSubscriptionSecretUpdateTO) {
        return eventSubscriptionService.findById(subscriptionID)
                .doOnNext(eventSubscription -> eventSubscription.setSecret(eventSubscriptionSecretUpdateTO.getSecret()))
                .flatMap(eventSubscriptionService::update)
                .then();
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return eventSubscriptionService.deleteById(id);
    }

    @Override
    public Mono<Void> delete(EventSubscriptionTO eventSubscriptionTO) {
        return eventSubscriptionService.deleteById(eventSubscriptionTO.getSubscriptionID());
    }

    @Override
    public UUID getIdOfEntity(EventSubscriptionTO entity) {
        return entity.getSubscriptionID();
    }

    @Override
    public Flux<EventSubscriptionTO> findAllExtended(ExtendedRequest<EventSubscription> extendedRequest) {
        return mapManyD2TO(eventSubscriptionService.findAll());
    }

    private Mono<EventSubscriptionTO> createEventTypes(EventSubscriptionTO eventSubscriptionTO) {
        List<EventType> eventTypeList = eventSubscriptionTO.getEventType();
        if (eventTypeList == null || eventTypeList.isEmpty()) {
            eventTypeList = ALL_EVENT_TYPES;
            eventSubscriptionTO.setEventType(eventTypeList);
        }
        return Flux.fromIterable(eventTypeList)
                .concatMap(eventType -> eventSubscriptionRepository.insertEventTypeForSubscription(eventSubscriptionTO.getSubscriptionID(), eventType))
                .then(Mono.just(eventSubscriptionTO));
    }

    private Mono<EventSubscriptionTO> mapSingleD2TO(Mono<EventSubscription> eventSubscriptionMono) {
        return eventSubscriptionMono
                .map(eventSubscription -> MappingUtils.instanceFrom(eventSubscription, EventSubscriptionTO::new, AbstractEventSubscription.class))
                .flatMap(eventSubscriptionTO ->
                        eventSubscriptionRepository.findEventTypesForSubscription(eventSubscriptionTO.getSubscriptionID())
                                .map(EventType::valueOf)
                                .collectList()
                                .doOnNext(eventSubscriptionTO::setEventType)
                                .thenReturn(eventSubscriptionTO)
                );
    }

    private Flux<EventSubscriptionTO> mapManyD2TO(Flux<EventSubscription> eventSubscriptionFlux) {
        return eventSubscriptionFlux
                .map(eventSubscription -> MappingUtils.instanceFrom(eventSubscription, EventSubscriptionTO::new, AbstractEventSubscription.class))
                .collectList()
                .flatMapMany(eventSubscriptionList -> {
                    Map<UUID, EventSubscriptionTO> id2subscription = eventSubscriptionList.stream().collect(Collectors.toMap(
                            AbstractEventSubscription::getSubscriptionID,
                            Function.identity()
                    ));
                    return Flux.fromIterable(eventSubscriptionList)
                            .doOnNext(eventSubscriptionTO -> eventSubscriptionTO.setEventType(new ArrayList<>()))
                            .map(AbstractEventSubscription::getSubscriptionID)
                            .buffer(MappingUtils.SQL_LIST_BUFFER_SIZE)
                            .concatMap(eventSubscriptionRepository::findEventTypesForSubscriptionIDIn)
                            .doOnNext(eventSubscriptionEventType -> {
                                EventSubscriptionTO subscriptionTO = id2subscription.get(eventSubscriptionEventType.getSubscriptionID());
                                assert subscriptionTO != null;
                                subscriptionTO.getEventType().add(eventSubscriptionEventType.getEventType());
                            })
                            .thenMany(Flux.fromIterable(eventSubscriptionList));
                });
    }
}
