package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.extendedrequest.ExtendedRequest;
import org.dcsa.core.service.impl.BaseServiceImpl;
import org.dcsa.core.util.MappingUtils;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.tnt.model.base.AbstractEventSubscription;
import org.dcsa.tnt.model.transferobjects.EventSubscriptionTO;
import org.dcsa.tnt.service.EventSubscriptionService;
import org.dcsa.tnt.service.EventSubscriptionTOService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventSubscriptionTOServiceImpl extends BaseServiceImpl<EventSubscriptionTO, UUID> implements EventSubscriptionTOService {

    private final EventSubscriptionService eventSubscriptionService;

    @Override
    public Flux<EventSubscriptionTO> findAll() {
        return eventSubscriptionService.findAll()
                .map(eventSubscription -> MappingUtils.instanceFrom(eventSubscription, EventSubscriptionTO::new, AbstractEventSubscription.class));
    }

    @Override
    public Mono<EventSubscriptionTO> findById(UUID id) {
        return eventSubscriptionService.findById(id)
                .map(eventSubscription -> MappingUtils.instanceFrom(eventSubscription, EventSubscriptionTO::new, AbstractEventSubscription.class));
    }

    @Override
    public Mono<EventSubscriptionTO> create(EventSubscriptionTO eventSubscriptionTO) {
        return eventSubscriptionService.create(MappingUtils.instanceFrom(eventSubscriptionTO, EventSubscription::new, AbstractEventSubscription.class))
                .map(eventSubscription -> MappingUtils.instanceFrom(eventSubscription, EventSubscriptionTO::new, AbstractEventSubscription.class));
    }

    @Override
    public Mono<EventSubscriptionTO> update(EventSubscriptionTO eventSubscriptionTO) {
        return eventSubscriptionService.update(MappingUtils.instanceFrom(eventSubscriptionTO, EventSubscription::new, AbstractEventSubscription.class))
                .map(eventSubscription -> MappingUtils.instanceFrom(eventSubscription, EventSubscriptionTO::new, AbstractEventSubscription.class));
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return eventSubscriptionService.deleteById(id);
    }

    @Override
    public Mono<Void> delete(EventSubscriptionTO eventSubscriptionTO) {
        return eventSubscriptionService.deleteById(eventSubscriptionTO.getId());
    }

    @Override
    public UUID getIdOfEntity(EventSubscriptionTO entity) {
        return entity.getId();
    }

    @Override
    public Flux<EventSubscriptionTO> findAllExtended(ExtendedRequest<EventSubscription> extendedRequest) {
        return eventSubscriptionService.findAll()
                .map(eventSubscription -> MappingUtils.instanceFrom(eventSubscription, EventSubscriptionTO::new, AbstractEventSubscription.class));
    }
}
