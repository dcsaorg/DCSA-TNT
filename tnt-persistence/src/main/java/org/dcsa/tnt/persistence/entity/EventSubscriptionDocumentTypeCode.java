package org.dcsa.tnt.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dcsa.tnt.persistence.entity.EventSubscription.EventSubscriptionEnumSetItem;
import org.dcsa.tnt.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.tnt.persistence.entity.enums.EquipmentEventTypeCode;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "event_subscription_document_type_code")
public class EventSubscriptionDocumentTypeCode implements EventSubscriptionEnumSetItem<DocumentTypeCode> {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Setter(AccessLevel.PRIVATE)
  public static class EventSubscriptionDocumentTypeCodePk implements Serializable {
    @Column(name = "subscription_id", nullable = false)
    private UUID subscriptionID;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type_code", nullable = false)
    private DocumentTypeCode documentTypeCode;
  }

  @EmbeddedId
  private EventSubscriptionDocumentTypeCodePk pk;

  public EventSubscriptionDocumentTypeCode(UUID subscriptionId, DocumentTypeCode code) {
    pk = new EventSubscriptionDocumentTypeCodePk(subscriptionId, code);
  }

  @Transient
  public DocumentTypeCode getValue() {
    return pk.documentTypeCode;
  }
}
