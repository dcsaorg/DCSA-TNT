package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.exception.UpdateException;
import org.dcsa.core.service.impl.ExtendedBaseServiceImpl;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.tnt.repository.EventSubscriptionRepository;
import org.dcsa.tnt.service.EventSubscriptionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventSubscriptionServiceImpl extends ExtendedBaseServiceImpl<EventSubscriptionRepository, EventSubscription, UUID> implements EventSubscriptionService {
    private final EventSubscriptionRepository eventSubscriptionRepository;

    @Override
    public EventSubscriptionRepository getRepository() {
        return eventSubscriptionRepository;
    }

    @Override
    public Class<EventSubscription> getModelClass() {
        return EventSubscription.class;
    }

    @Override
    protected Mono<EventSubscription> preSaveHook(EventSubscription eventSubscription) {
        // Ensure that the callback url at least looks valid.
        try {
            new URI(eventSubscription.getCallbackUrl());
        } catch (URISyntaxException e) {
            return Mono.error(new UpdateException("callbackUrl is invalid: " + e.getLocalizedMessage()));
        }
        return super.preSaveHook(eventSubscription);
    }
}
