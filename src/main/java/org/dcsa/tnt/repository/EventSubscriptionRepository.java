package org.dcsa.tnt.repository;

import org.dcsa.core.events.model.enums.EventType;
import org.dcsa.core.repository.ExtendedRepository;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.tnt.model.EventSubscriptionEventType;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface EventSubscriptionRepository extends ExtendedRepository<EventSubscription, UUID> {

    @Query("SELECT es.* FROM event_subscription es"
            + "  JOIN event_subscription_event_types eset ON (es.subscription_id = eset.subscription_id)"
            + " WHERE eset.event_type = :eventType AND (:equipmentReference IS NULL or es.equipment_reference = :equipmentReference)")
    Flux<EventSubscription> findSubscriptionsByFilters(@Param("eventType") EventType eventType, @Param("equipmentReference") String equipmentReference);

    @Modifying
    @Query("INSERT INTO event_subscription_event_types (subscription_id, event_type)"
            + " VALUES (:subscriptionID, :eventType)")
    Mono<Void> insertEventTypeForSubscription(UUID subscriptionID, EventType eventType);

    @Query("SELECT event_type FROM event_subscription_event_types"
            + " WHERE subscription_id = :subscriptionID")
    Flux<String> findEventTypesForSubscription(UUID subscriptionID);


    @Modifying
    @Query("DELETE FROM event_subscription_event_types WHERE subscription_id = :subscriptionID")
    Mono<Void> deleteEventTypesForSubscription(UUID subscriptionID);

    @Query("SELECT event_subscription_event_types.* FROM event_subscription_event_types"
            + " WHERE subscription_id IN (:subscriptionIDs)"
            + " ORDER BY event_subscription_id, event_type"
    )
    Flux<EventSubscriptionEventType> findEventTypesForSubscriptionIDIn(List<UUID> subscriptionIDs);
}
