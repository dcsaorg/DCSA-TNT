package org.dcsa.tnt.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.tnt.domain.valueobjects.enums.DocumentReferenceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "tnt_event_document_reference")
public class EventDocumentReference {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "event_id", nullable = false, length = 100)
  private String eventId;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private DocumentReferenceType type;

  @Column(name = "reference", nullable = false, length = 100)
  private String value;
}
