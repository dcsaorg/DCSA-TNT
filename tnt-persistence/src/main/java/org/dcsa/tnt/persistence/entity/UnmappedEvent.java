package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "unmapped_event_queue")
public class UnmappedEvent implements Persistable<UUID> {
  @Id
  @Column(name = "event_id", nullable = false)
  private UUID eventID;

  private transient boolean newRecord;

  @Column(name = "enqueued_at_date_time", nullable = false)
  private OffsetDateTime enqueuedAtDateTime;

  @Override
  public UUID getId() {
    return eventID;
  }

  @Override
  public boolean isNew() {
    return eventID == null || newRecord;
  }
}
