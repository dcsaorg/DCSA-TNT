package org.dcsa.tnt.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.tnt.persistence.entity.enums.ReferenceType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
public class Reference {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(name = "reference_type_code", nullable = false)
  private ReferenceType referenceType;

  @Column(name = "reference_value", length = 100, nullable = false)
  private String referenceValue;

  @Column(name = "shipment_id")
  private UUID shipmentID;

  @Column(name = "shipping_instruction_id")
  private UUID shippingInstructionID;

  @Column(name = "booking_id")
  private UUID bookingID;

  @Column(name = "consignment_item_id")
  private UUID consignmentItemID;
}
