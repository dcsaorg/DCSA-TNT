package org.dcsa.tnt.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dcsa.tnt.domain.persistence.entity.EventSubscription.EventSubscriptionEnumSetItem;
import org.dcsa.tnt.domain.valueobjects.enums.TransportEventTypeCode;

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
