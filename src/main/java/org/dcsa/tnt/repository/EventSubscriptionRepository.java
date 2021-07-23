package org.dcsa.tnt.repository;

import org.dcsa.core.events.model.enums.EventType;
import org.dcsa.core.events.model.enums.TransportEventTypeCode;
import org.dcsa.core.repository.ExtendedRepository;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.tnt.model.EventSubscriptionEventType;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface EventSubscriptionRepository extends ExtendedRepository<EventSubscription, UUID> {

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
            + " ORDER BY subscription_id, event_type"
    )
    Flux<EventSubscriptionEventType> findEventTypesForSubscriptionIDIn(List<UUID> subscriptionIDs);

    // TODO: Make query and specify relevant parameters
    @Query("")
    Flux<EventSubscription> findByEquipmentEventFields(
            String carrierBookingReference, String equipmentReference, String shipmentEventTypeCode,
            String carrierServiceCode, String carrierVoyageNumber, String vesselIMONumber,
            String transportDocumentReference, String transportEventTypeCode, String transportDocumentTypeCode,
            String transportCallID, String scheduleID, EventType eventType);

    // TODO: Make query and specify relevant parameters
    @Query("")
    Flux<EventSubscription> findByShipmentEventFields(
            String carrierBookingReference, String equipmentReference, String shipmentEventTypeCode,
            String carrierServiceCode, String carrierVoyageNumber, String vesselIMONumber,
            String transportDocumentReference, String transportEventTypeCode, String transportDocumentTypeCode,
            String transportCallID, String scheduleID, EventType eventType);

    // TODO: Find out what happens with 'IN' empty list
    @Query("SELECT event_subscription.* FROM event_subscription"
            + " JOIN event_subscription_event_types"
            + "   ON event_subscription_event_types.subscription_id = event_subscription.subscription_id"
            + " WHERE event_type = :eventType"
            + "   AND shipment_event_type_code IS NULL"
            + "   AND (carrier_booking_reference IS NULL OR carrier_booking_reference IN :carrierBookingReferences)"
            + "   AND (transport_document_reference IS NULL OR transport_document_reference IN :transportDocumentReferences)"
            + "   AND (transport_document_type_code IS NULL OR transport_document_type_code IN :transportDocumentTypeCodes)"
            + "   AND (transport_event_type_code IS NULL OR transport_event_type_code = :transportEventTypeCode)"
            + "   AND (transport_call_id IS NULL OR transport_call_id = :transportCallID)"
            + "   AND (vessel_imo_number IS NULL OR vessel_imo_number = :vesselIMONumber)"
            + "   AND (carrier_voyage_number IS NULL OR carrier_voyage_number IN :carrierVoyageNumbers)"
            + "   AND (carrier_service_code IS NULL OR carrier_service_code IN :carrierServiceCodes)"
            + "   AND equipmentEventTypeCode IS NULL"
            + "   AND equipmentReference IS NULL")
    Flux<EventSubscription> findByTransportEventFields(
            List<String> carrierVoyageNumbers, List<String> carrierServiceCodes,
            TransportEventTypeCode transportEventTypeCode, String vesselIMONumber, String transportCallID,
            List<String> carrierBookingReferences, List<String> transportDocumentReferences,
            List<String> transportDocumentTypeCodes,
            EventType eventType);
}
