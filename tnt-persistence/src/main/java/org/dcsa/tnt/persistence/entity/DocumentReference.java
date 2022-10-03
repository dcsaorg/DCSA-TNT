package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "event_document_reference")
public class DocumentReference {

  @Column(name = "transport_call_id")
  private UUID transportCallID;

  @Column(name = "document_id")
  private UUID documentID;

  @Id // This is not a real ID (just a workaround) - Done to satisfy JPA Entity ID requirement
  // Note that this is a VIEW -- NOT a schema
  @Column(name = "link_type")
  private String linkType;

  @Column(name = "document_reference_type")
  private String documentReferenceType;

  @Column(name = "document_reference_value")
  private String documentReferenceValue;

  @Column(name = "carrier_booking_request_reference")
  private String carrierBookingRequestReference;

  @Column(name = "carrier_booking_reference")
  private String carrierBookingReference;

  @Column(name = "transport_document_reference")
  private String transportDocumentReference;
}
