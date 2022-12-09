package org.dcsa.tnt.persistence.entity;

import lombok.*;
import org.dcsa.tnt.persistence.entity.enums.EventType;

import javax.persistence.*;
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
