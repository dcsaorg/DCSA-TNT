package org.dcsa.tnt.persistence.entity;

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
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "shipment_event")
public class ShipmentEvent extends Event {
  @Enumerated(EnumType.STRING)
  @Column(name = "shipment_event_type_code")
  private ShipmentEventTypeCode shipmentEventTypeCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "document_type_code")
  private DocumentTypeCode documentTypeCode;

  @Column(name = "document_id")
  private UUID documentID;

  @Column(name = "reason", length = 100)
  private String reason;
}
