package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.tnt.persistence.entity.enums.ReferenceType;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "event_reference") // This is a view
public class EventReference {

  @Id
  @Column(name = "view_id")
  private UUID viewID;

  @Enumerated(EnumType.STRING)
  @Column(name = "reference_type_code", nullable = false)
  private ReferenceType referenceType;

  @Column(name = "reference_value", length = 100, nullable = false)
  private String referenceValue;

  @Column(name = "utilized_transport_equipment_id")
  private UUID utilizedEquipmentID;

  @Column(name = "document_id")
  private UUID documentID;

  @Column(name = "transport_call_id")
  private UUID transportCallID;

  @Column(name = "link_type")
  private String linkType;
}
