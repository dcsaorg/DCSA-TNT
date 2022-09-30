package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dcsa.tnt.persistence.entity.EventSubscription.EventSubscriptionEnumSetItem;
import org.dcsa.tnt.persistence.entity.enums.EquipmentEventTypeCode;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
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
