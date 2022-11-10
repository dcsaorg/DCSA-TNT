package org.dcsa.tnt.persistence.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.dcsa.tnt.persistence.entity.enums.EventClassifierCode;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;
import java.util.UUID;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@MappedSuperclass
@SuperBuilder
public abstract class Event {
  @Id
  @Column(name = "event_id", nullable = false)
  private UUID eventID;

  @Column(name = "event_date_time", nullable = false)
  private OffsetDateTime eventDateTime;

  @CreatedDate
  @Column(name = "event_created_date_time", nullable = false)
  private OffsetDateTime eventCreatedDateTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "event_classifier_code", nullable = false)
  private EventClassifierCode eventClassifierCode;
}
