package org.dcsa.tnt.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dcsa.tnt.domain.persistence.entity.EventSubscription.EventSubscriptionEnumSetItem;
import org.dcsa.tnt.domain.valueobjects.enums.DocumentTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
