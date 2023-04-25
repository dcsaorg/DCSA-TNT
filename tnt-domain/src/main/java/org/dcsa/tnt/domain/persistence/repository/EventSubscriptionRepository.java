package org.dcsa.tnt.domain.persistence.repository;

import org.dcsa.tnt.domain.persistence.entity.EventSubscription;
import org.dcsa.tnt.domain.valueobjects.enums.DocumentTypeCode;
import org.dcsa.tnt.domain.valueobjects.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.domain.valueobjects.enums.EventType;
import org.dcsa.tnt.domain.valueobjects.enums.ShipmentEventTypeCode;
import org.dcsa.tnt.domain.valueobjects.enums.TransportEventTypeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventSubscriptionRepository extends JpaRepository<EventSubscription, UUID> {
  @Modifying
  @Query("UPDATE EventSubscription SET secret = :secret WHERE subscriptionID = :subscriptionID")
  void updateSecret(UUID subscriptionID, byte[] secret);

  @Modifying
  @Query("DELETE FROM EventSubscriptionEventType WHERE pk.subscriptionID = :subscriptionID AND pk.eventType = :eventType")
  void deleteEventType(UUID subscriptionID, EventType eventType);

  @Modifying
  @Query("DELETE FROM EventSubscriptionEquipmentEventTypeCode WHERE pk.subscriptionID = :subscriptionID AND pk.equipmentEventTypeCode = :equipmentEventTypeCode")
  void deleteEquipmentEventTypeCode(UUID subscriptionID, EquipmentEventTypeCode equipmentEventTypeCode);

  @Modifying
  @Query("DELETE FROM EventSubscriptionTransportEventTypeCode WHERE pk.subscriptionID = :subscriptionID AND pk.transportEventTypeCode = :transportEventTypeCode")
  void deleteTransportEventTypeCode(UUID subscriptionID, TransportEventTypeCode transportEventTypeCode);

  @Modifying
  @Query("DELETE FROM EventSubscriptionShipmentEventTypeCode WHERE pk.subscriptionID = :subscriptionID AND pk.shipmentEventTypeCode = :shipmentEventTypeCode")
  void deleteShipmentEventTypeCode(UUID subscriptionID, ShipmentEventTypeCode shipmentEventTypeCode);

  @Modifying
  @Query("DELETE FROM EventSubscriptionDocumentTypeCode WHERE pk.subscriptionID = :subscriptionID AND pk.documentTypeCode = :documentTypeCode")
  void deleteDocumentTypeCode(UUID subscriptionID, DocumentTypeCode documentTypeCode);
}
