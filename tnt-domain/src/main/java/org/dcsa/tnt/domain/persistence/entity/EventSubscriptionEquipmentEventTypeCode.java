package org.dcsa.tnt.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dcsa.tnt.domain.persistence.entity.EventSubscription.EventSubscriptionEnumSetItem;
import org.dcsa.tnt.domain.valueobjects.enums.EquipmentEventTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "event_subscription_equipment_event_type_code")
public class EventSubscriptionEquipmentEventTypeCode implements EventSubscriptionEnumSetItem<EquipmentEventTypeCode> {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Setter(AccessLevel.PRIVATE)
  public static class EventSubscriptionEquipmentEventTypeCodePk implements Serializable {
    @Column(name = "subscription_id", nullable = false)
    private UUID subscriptionID;

    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_event_type_code", nullable = false)
    private EquipmentEventTypeCode equipmentEventTypeCode;
  }

  @EmbeddedId
  private EventSubscriptionEquipmentEventTypeCodePk pk;

  public EventSubscriptionEquipmentEventTypeCode(UUID subscriptionId, EquipmentEventTypeCode code) {
    pk = new EventSubscriptionEquipmentEventTypeCodePk(subscriptionId, code);
  }

  @Transient
  public EquipmentEventTypeCode getValue() {
    return pk.equipmentEventTypeCode;
  }
}
