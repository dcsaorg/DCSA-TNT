package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.tnt.persistence.entity.enums.EventType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.UUID;

@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "event_cache_queue_dead")
@Getter
public class EventCacheQueueDead {
  @Id
  @Column(name = "event_id", nullable = false)
  private UUID eventID;

  @Enumerated(EnumType.STRING)
  @Column(name = "event_type", nullable = false)
  private EventType eventType;

  @Column(name = "failure_reason_type")
  private String failureReasonType;

  @Column(name = "failure_reason_message")
  private String failureReasonMessage;

  public static EventCacheQueueDead from(EventCacheQueue eventCacheQueue, Exception cause) {
    return EventCacheQueueDead.builder()
      .eventID(eventCacheQueue.getEventID())
      .eventType(eventCacheQueue.getEventType())
      .failureReasonType(cause.getClass().getName())
      .failureReasonMessage(cause.getMessage())
      .build();
  }
}
