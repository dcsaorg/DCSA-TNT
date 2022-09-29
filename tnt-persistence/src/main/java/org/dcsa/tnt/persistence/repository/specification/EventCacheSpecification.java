package org.dcsa.tnt.persistence.repository.specification;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.dcsa.tnt.persistence.entity.EquipmentEvent_;
import org.dcsa.tnt.persistence.entity.EventCache;
import org.dcsa.tnt.persistence.entity.EventCache_;
import org.dcsa.tnt.persistence.entity.Service_;
import org.dcsa.tnt.persistence.entity.ShipmentEvent_;
import org.dcsa.tnt.persistence.entity.TransportCall_;
import org.dcsa.tnt.persistence.entity.TransportEvent_;
import org.dcsa.tnt.persistence.entity.Vessel_;
import org.dcsa.tnt.persistence.entity.Voyage_;
import org.dcsa.tnt.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.tnt.persistence.entity.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.persistence.entity.enums.EventType;
import org.dcsa.tnt.persistence.entity.enums.ShipmentEventTypeCode;
import org.dcsa.tnt.persistence.entity.enums.TransportEventTypeCode;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class EventCacheSpecification {
  public record EventCacheFilters(
    List<EventType> eventType,
    ShipmentEventTypeCode shipmentEventTypeCode,
    String carrierBookingReference,
    String transportDocumentReference,
    TransportEventTypeCode transportEventTypeCode,
    String transportCallID,
    String vesselIMONumber,
    String carrierVoyageNumber,
    String carrierServiceCode,
    EquipmentEventTypeCode equipmentEventTypeCode,
    String equipmentReference
  ) {
    @Builder
    public EventCacheFilters { }
  }

  public static Specification<EventCache> withFilters(final EventCacheFilters filters) {
    return (Root<EventCache> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      JsonPathExpressionBuilder jsonPath = new JsonPathExpressionBuilder(root, builder, EventCache_.CONTENT);

      if (filters.eventType != null) {
        predicates.add(root.get(EventCache_.EVENT_TYPE).in(filters.eventType));
      }

      if (filters.shipmentEventTypeCode != null) {
        predicates.add(
          builder.equal(
            jsonPath.of(ShipmentEvent_.SHIPMENT_EVENT_TYPE_CODE),
            builder.literal(filters.shipmentEventTypeCode.name())
          )
        );
      }

      if (filters.carrierBookingReference != null) {
        predicates.add(
          builder.like(root.get(EventCache_.DOCUMENT_REFERENCES), "%|BKG=" + filters.carrierBookingReference+ "|%")
        );
      }

      if (filters.transportDocumentReference != null) {
        predicates.add(
          builder.or(
            builder.like(root.get(EventCache_.DOCUMENT_REFERENCES), "%|TRD=" + filters.transportDocumentReference + "|%"),
            builder.and(
              builder.equal(
                jsonPath.of(ShipmentEvent_.DOCUMENT_TYPE_CODE),
                builder.literal(DocumentTypeCode.TRD.name())
              ),
              builder.equal(
                jsonPath.of(ShipmentEvent_.DOCUMENT_ID),
                builder.literal(filters.transportDocumentReference)
              )
            )
          )
        );
      }

      if (filters.transportEventTypeCode != null) {
        predicates.add(
          builder.equal(
            jsonPath.of(TransportEvent_.TRANSPORT_EVENT_TYPE_CODE),
            builder.literal(filters.transportEventTypeCode.name())
          )
        );
      }

      if (filters.transportCallID != null) {
        predicates.add(
          builder.equal(
            jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.ID),
            builder.literal(filters.transportCallID)
          )
        );
      }

      if (filters.vesselIMONumber != null) {
        predicates.add(
          builder.equal(
            jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.VESSEL, Vessel_.VESSEL_IM_ONUMBER),
            builder.literal(filters.vesselIMONumber)
          )
        );
      }

      if (filters.carrierVoyageNumber != null) {
        predicates.add(
          builder.or(
            builder.equal(
              jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.IMPORT_VOYAGE, Voyage_.CARRIER_VOYAGE_NUMBER),
              builder.literal(filters.carrierVoyageNumber)
            ),
            builder.equal(
              jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.EXPORT_VOYAGE, Voyage_.CARRIER_VOYAGE_NUMBER),
              builder.literal(filters.carrierVoyageNumber)
            )
          )
        );
      }

      if (filters.carrierServiceCode != null) {
        predicates.add(
          builder.or(
            builder.equal(
              jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.IMPORT_VOYAGE, Voyage_.SERVICE, Service_.CARRIER_SERVICE_CODE),
              builder.literal(filters.carrierServiceCode)
            ),
            builder.equal(
              jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.EXPORT_VOYAGE, Voyage_.SERVICE, Service_.CARRIER_SERVICE_CODE),
              builder.literal(filters.carrierServiceCode)
            )
          )
        );
      }

      if (filters.equipmentEventTypeCode != null) {
        predicates.add(
          builder.equal(
            jsonPath.of(EquipmentEvent_.EQUIPMENT_EVENT_TYPE_CODE),
            builder.literal(filters.equipmentEventTypeCode.name())
          )
        );
      }

      if (filters.equipmentReference != null) {
        predicates.add(
          builder.equal(
            jsonPath.of(EquipmentEvent_.EQUIPMENT_REFERENCE),
            builder.literal(filters.equipmentReference)
          )
        );
      }

      return builder.and(predicates.toArray(Predicate[]::new));
    };
  }

  @RequiredArgsConstructor
  private static class JsonPathExpressionBuilder {
    private final Root<EventCache> root;
    private final CriteriaBuilder builder;
    private final String jsonFieldName;

    public Expression<String> of(String... jsonPathElements) {
      List<Expression<?>> expressions = new ArrayList<>();
      expressions.add(root.get(jsonFieldName));
      for (String e : jsonPathElements) {
        expressions.add(builder.literal(e));
      }
      return builder.function("jsonb_extract_path_text", String.class, expressions.toArray(Expression[]::new));
    }
  }
}
