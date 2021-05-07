package org.dcsa.tnt.service;

import org.dcsa.core.extendedrequest.ExtendedRequest;
import org.dcsa.core.service.BaseService;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.tnt.model.transferobjects.EventSubscriptionSecretUpdateTO;
import org.dcsa.tnt.model.transferobjects.EventSubscriptionTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EventSubscriptionTOService extends BaseService<EventSubscriptionTO, UUID> {

    Flux<EventSubscriptionTO> findAllExtended(ExtendedRequest<EventSubscription> extendedRequest);
    Mono<Void> updateSecret(UUID subscriptionID, EventSubscriptionSecretUpdateTO eventSubscriptionSecretUpdateTO);
}
