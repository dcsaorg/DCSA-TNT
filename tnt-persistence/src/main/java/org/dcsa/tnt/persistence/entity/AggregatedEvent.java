package org.dcsa.tnt.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.tnt.persistence.entity.enums.EventClassifierCode;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "aggregated_events")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "event_type")
public abstract class AggregatedEvent {
  @Id
  @Column(name = "event_id", nullable = false)
  private UUID eventID;

  @Column(name = "event_date_time")
  private OffsetDateTime eventDateTime;

  @CreatedDate
  @Column(name = "event_created_date_time")
  private OffsetDateTime eventCreatedDateTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "event_classifier_code")
  private EventClassifierCode eventClassifierCode;
}