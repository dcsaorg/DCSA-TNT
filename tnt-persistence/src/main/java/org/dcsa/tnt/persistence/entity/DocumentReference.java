package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.RowId;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "event_document_reference") // This is a VIEW -- NOT a table
public class DocumentReference {

  @Id // This ID is added to the view to satisfy JPA primary key requirement
  @Column(name = "random_id")
  UUID id;

  @Column(name = "transport_call_id")
  private UUID transportCallID;

  @Column(name = "document_id")
  private UUID documentID;

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
