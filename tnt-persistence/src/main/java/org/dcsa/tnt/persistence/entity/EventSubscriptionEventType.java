package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dcsa.tnt.persistence.entity.EventSubscription.EventSubscriptionEnumSetItem;
import org.dcsa.tnt.persistence.entity.enums.EventType;

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
@Table(name = "event_subscription_event_type")
public class EventSubscriptionEventType implements EventSubscriptionEnumSetItem<EventType> {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Setter(AccessLevel.PRIVATE)
  public static class EventSubscriptionEventTypePk implements Serializable {
    @Column(name = "subscription_id", nullable = false)
    private UUID subscriptionID;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;
  }

  @EmbeddedId
  private EventSubscriptionEventTypePk pk;

  public EventSubscriptionEventType(UUID subscriptionId, EventType code) {
    pk = new EventSubscriptionEventTypePk(subscriptionId, code);
  }

  @Transient
  public EventType getValue() {
    return pk.eventType;
  }
}
