package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dcsa.tnt.persistence.entity.EventSubscription.EventSubscriptionEnumSetItem;
import org.dcsa.tnt.persistence.entity.enums.ShipmentEventTypeCode;
import org.dcsa.tnt.persistence.entity.enums.TransportEventTypeCode;

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
@Table(name = "event_subscription_transport_event_type_code")
public class EventSubscriptionTransportEventTypeCode implements EventSubscriptionEnumSetItem<TransportEventTypeCode> {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Setter(AccessLevel.PRIVATE)
  public static class EventSubscriptionTransportEventTypeCodePk implements Serializable {
    @Column(name = "subscription_id", nullable = false)
    private UUID subscriptionID;

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_event_type_code", nullable = false)
    private TransportEventTypeCode transportEventTypeCode;
  }

  @EmbeddedId
  private EventSubscriptionTransportEventTypeCodePk pk;

  public EventSubscriptionTransportEventTypeCode(UUID subscriptionId, TransportEventTypeCode code) {
    pk = new EventSubscriptionTransportEventTypeCodePk(subscriptionId, code);
  }

  @Transient
  public TransportEventTypeCode getValue() {
    return pk.transportEventTypeCode;
  }
}
