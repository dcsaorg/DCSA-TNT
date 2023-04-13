package org.dcsa.tnt.domain.persistence.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dcsa.tnt.domain.valueobjects.DomainEvent;
import org.hibernate.annotations.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "tnt_event")
public class Event {
  @Id
  @Column(name = "event_id", nullable = false, length = 100)
  private String eventId;

  // @Type(JsonBinaryType.class)
  @Type(JsonBinaryType.class)
  @Column(name = "content", columnDefinition = "jsonb", nullable = false)
  private DomainEvent content;

  @Column(name = "event_type", nullable = false, length = 16)
  private String eventType;

  @Column(name = "event_created_date_time", nullable = false)
  private OffsetDateTime eventCreatedDateTime;

  @Column(name = "event_date_time")
  private OffsetDateTime eventDateTime;

  @Column(name = "transport_event_type_code")
  private String transportEventTypeCode;

  @Column(name = "equipment_event_type_code")
  private String equipmentEventTypeCode;

  @Column(name = "shipment_event_type_code")
  private String shipmentEventTypeCode;

  @Column(name = "document_type_code")
  private String documentTypeCode;

  @Column(name = "vessel_imo_number")
  private String vesselIMONumber;

  @Column(name = "transport_call_reference")
  private String transportCallReference;

  @Column(name = "carrier_export_voyage_number")
  private String carrierExportVoyageNumber;

  @Column(name = "universal_export_voyage_reference")
  private String universalExportVoyageReference;

  @Column(name = "carrier_service_code")
  private String carrierServiceCode;

  @Column(name = "universal_service_reference")
  private String universalServiceReference;

  @Column(name = "un_location_code")
  private String UNLocationCode;

  @Column(name = "equipment_reference")
  private String equipmentReference;
}
