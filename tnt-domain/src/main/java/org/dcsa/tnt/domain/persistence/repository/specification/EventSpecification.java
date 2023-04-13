package org.dcsa.tnt.domain.persistence.repository.specification;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.skernel.infrastructure.http.queryparams.ParsedQueryParameter;
import org.dcsa.tnt.domain.persistence.entity.Event;
import org.dcsa.tnt.domain.persistence.entity.EventDocumentReference;
import org.dcsa.tnt.domain.persistence.entity.EventDocumentReference_;
import org.dcsa.tnt.domain.persistence.entity.EventReference;
import org.dcsa.tnt.domain.persistence.entity.EventReference_;
import org.dcsa.tnt.domain.persistence.entity.Event_;
import org.dcsa.tnt.domain.valueobjects.enums.DocumentTypeCode;
import org.dcsa.tnt.domain.valueobjects.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.domain.valueobjects.enums.EventType;
import org.dcsa.tnt.domain.valueobjects.enums.ReferenceType;
import org.dcsa.tnt.domain.valueobjects.enums.ShipmentEventTypeCode;
import org.dcsa.tnt.domain.valueobjects.enums.TransportEventTypeCode;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

@Slf4j
@UtilityClass
public class EventSpecification {

  public record EventFilters(
    List<ParsedQueryParameter<OffsetDateTime>> eventCreatedDateTime,
    List<ParsedQueryParameter<OffsetDateTime>> eventDateTime,
    Set<EventType> eventType,
    Set<ShipmentEventTypeCode> shipmentEventTypeCode,
    Set<TransportEventTypeCode> transportEventTypeCode,
    Set<EquipmentEventTypeCode> equipmentEventTypeCode,
    Set<DocumentTypeCode> documentTypeCode,
    String documentReference,
    String equipmentReference,
    String transportCallReference,
    String vesselIMONumber,
    String carrierExportVoyageNumber,
    String universalExportVoyageReference,
    String carrierServiceCode,
    String universalServiceReference,
    String UNLocationCode
  ) {
    @Builder
    public EventFilters { }
  }

  public static Specification<Event> withFilters(final EventFilters filters) {
    log.debug("Searching based on {}", filters);

    return (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      BiConsumer<String, Set<? extends Enum<?>>> addEnumRestriction = (fieldName, filterValues) -> {
        if (filterValues != null && !filterValues.isEmpty()) {
          predicates.add(root.get(fieldName).in(filterValues.stream().map(Enum::name).toList()));
        }
      };
      BiConsumer<String, Object> addEquals = (fieldName, filterValue) -> {
        if (filterValue != null) {
          predicates.add(builder.equal(root.get(fieldName), filterValue));
        }
      };

      handleParsedQueryParameter(predicates, builder, root.get(Event_.EVENT_CREATED_DATE_TIME), filters.eventCreatedDateTime);
      handleParsedQueryParameter(predicates, builder, root.get(Event_.EVENT_DATE_TIME), filters.eventDateTime);

      addEnumRestriction.accept(Event_.EVENT_TYPE, filters.eventType());
      addEnumRestriction.accept(Event_.SHIPMENT_EVENT_TYPE_CODE, filters.shipmentEventTypeCode());
      addEnumRestriction.accept(Event_.TRANSPORT_EVENT_TYPE_CODE, filters.transportEventTypeCode());
      addEnumRestriction.accept(Event_.EQUIPMENT_EVENT_TYPE_CODE, filters.equipmentEventTypeCode());
      addEnumRestriction.accept(Event_.DOCUMENT_TYPE_CODE, filters.documentTypeCode());

      addEquals.accept(Event_.TRANSPORT_CALL_REFERENCE, filters.transportCallReference());
      addEquals.accept(Event_.VESSEL_IM_ONUMBER, filters.vesselIMONumber());
      addEquals.accept(Event_.CARRIER_EXPORT_VOYAGE_NUMBER, filters.carrierExportVoyageNumber());
      addEquals.accept(Event_.UNIVERSAL_EXPORT_VOYAGE_REFERENCE, filters.universalExportVoyageReference());
      addEquals.accept(Event_.U_NLOCATION_CODE, filters.UNLocationCode());
      addEquals.accept(Event_.CARRIER_SERVICE_CODE, filters.carrierServiceCode());
      addEquals.accept(Event_.UNIVERSAL_SERVICE_REFERENCE, filters.universalServiceReference());

      if (filters.equipmentReference() != null) {
        Subquery<EventReference> subQuery = query.subquery(EventReference.class);
        Root<EventReference> subRoot = subQuery.from(EventReference.class);
        subQuery.select(subRoot).where(
          builder.equal(root.get(Event_.EVENT_ID), subRoot.get(EventReference_.EVENT_ID)),
          builder.equal(subRoot.get(EventReference_.TYPE), ReferenceType.EQ),
          builder.equal(subRoot.get(EventReference_.VALUE), filters.equipmentReference())
        );
        predicates.add(
          builder.or(
            builder.equal(root.get(Event_.EQUIPMENT_REFERENCE), filters.equipmentReference()),
            builder.exists(subQuery))
          );
      }

      if (filters.documentReference() != null) {
        Subquery<EventDocumentReference> subQuery = query.subquery(EventDocumentReference.class);
        Root<EventDocumentReference> subRoot = subQuery.from(EventDocumentReference.class);
        subQuery.select(subRoot).where(
          builder.equal(root.get(Event_.EVENT_ID), subRoot.get(EventDocumentReference_.EVENT_ID)),
          builder.equal(subRoot.get(EventDocumentReference_.VALUE), filters.documentReference())
        );
        predicates.add(builder.exists(subQuery));
      }

      return builder.and(predicates.toArray(Predicate[]::new));
    };
  }

  private static <T extends Comparable<T>> void handleParsedQueryParameter(
    List<Predicate> predicates,
    CriteriaBuilder builder,
    Expression<T> field,
    List<ParsedQueryParameter<T>> filterValues
  ) {
    if (filterValues != null && !filterValues.isEmpty()) {
      predicates.add(builder.or(filterValues.stream()
        .map(pqp -> processParsedQueryParameter(builder, field, pqp))
        .toArray(Predicate[]::new)
      ));
    }
  }

  private static <T extends Comparable<T>> Predicate processParsedQueryParameter(
    CriteriaBuilder builder,
    Expression<T> field,
    ParsedQueryParameter<T> parsedQueryParameter
  ) {
    final T value = parsedQueryParameter.value();
    return switch (parsedQueryParameter.comparisonType()) {
      case EQ -> builder.equal(field, value);
      case GTE -> builder.greaterThanOrEqualTo(field, value);
      case GT -> builder.greaterThan(field, value);
      case LTE -> builder.lessThanOrEqualTo(field, value);
      case LT -> builder.lessThan(field, value);
    };
  }
}
