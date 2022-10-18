package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.tnt.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.tnt.persistence.entity.enums.ShipmentEventTypeCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "shipment_event")
public class ShipmentEvent extends Event {
  @Enumerated(EnumType.STRING)
  @Column(name = "shipment_event_type_code", nullable = false)
  private ShipmentEventTypeCode shipmentEventTypeCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "document_type_code", nullable = false)
  private DocumentTypeCode documentTypeCode;

  @Column(name = "document_id", nullable = false)
  private UUID documentID;

  @Column(name = "document_reference", nullable = false)
  private String documentReference;

  @Column(name = "reason", length = 100)
  private String reason;
}
