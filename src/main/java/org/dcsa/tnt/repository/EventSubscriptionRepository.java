package org.dcsa.tnt.repository;

import org.dcsa.core.repository.ExtendedRepository;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.tnt.model.enums.EventType;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface EventSubscriptionRepository extends ExtendedRepository<EventSubscription, UUID> {

    @Query("SELECT callback_url FROM \"dcsa_tnt_v2_0\".event_subscription a WHERE ((:eventType IS NULL or a.event_type =:eventType) AND (:equipmentReference IS NULL or a.equipment_reference =:equipmentReference)) OR a.event_type = ''")
    Flux<String> findSubscriptionsByFilters(@Param("eventType") EventType eventType, @Param("equipmentReference") String equipmentReference);
}
