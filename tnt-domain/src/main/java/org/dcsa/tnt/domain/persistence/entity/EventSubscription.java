package org.dcsa.tnt.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "event_subscription")
public class EventSubscription {
  @Id
  @GeneratedValue
  @Column(name = "subscription_id", nullable = false)
  private UUID subscriptionID;

  @Column(name = "callback_url", columnDefinition = "text", nullable = false)
  private String callbackUrl;

  @Column(name = "document_reference", length = 100)
  private String documentReference;

  @Column(name = "equipment_reference", length = 15)
  private String equipmentReference;

  @Column(name = "transport_call_reference", length = 100)
  private String transportCallReference;

  @Column(name = "vessel_imo_number", length = 7)
  private String vesselIMONumber;

  @Column(name = "carrier_export_voyage_number", length = 50)
  private String carrierExportVoyageNumber;

  @Column(name = "universal_export_voyage_reference", length = 5)
  private String universalExportVoyageReference;

  @Column(name = "carrier_service_code", length = 11)
  private String carrierServiceCode;

  @Column(name = "universal_service_reference", length = 8)
  private String universalServiceReference;

  @Column(name = "un_location_code", length = 5)
  private String UNLocationCode;

  @ToString.Exclude
  @Column(name = "secret", columnDefinition = "bytea", nullable = false)
  private byte[] secret;

  @CreatedDate
  @Column(name = "created_date_time", nullable = false)
  private OffsetDateTime createdDateTime;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "pk.subscriptionID", cascade = CascadeType.ALL)
  private Set<EventSubscriptionEventType> eventTypes;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "pk.subscriptionID", cascade = CascadeType.ALL)
  private Set<EventSubscriptionTransportEventTypeCode> transportEventTypeCodes;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "pk.subscriptionID", cascade = CascadeType.ALL)
  private Set<EventSubscriptionShipmentEventTypeCode> shipmentEventTypeCodes;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "pk.subscriptionID", cascade = CascadeType.ALL)
  private Set<EventSubscriptionEquipmentEventTypeCode> equipmentEventTypeCodes;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "pk.subscriptionID", cascade = CascadeType.ALL)
  private Set<EventSubscriptionDocumentTypeCode> documentTypeCodes;

  public interface EventSubscriptionEnumSetItem<T extends Enum<T>> {
    T getValue();
  }
}
