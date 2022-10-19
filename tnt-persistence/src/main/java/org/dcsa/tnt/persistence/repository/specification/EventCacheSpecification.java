package org.dcsa.tnt.persistence.repository.specification;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.skernel.domain.persistence.entity.Location_;
import org.dcsa.skernel.domain.persistence.entity.base.BaseTransportCall_;
import org.dcsa.skernel.infrastructure.http.queryparams.ParsedQueryParameter;
import org.dcsa.tnt.persistence.entity.*;
import org.dcsa.tnt.persistence.entity.enums.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;

@Slf4j
@UtilityClass
public class EventCacheSpecification {

  public record EventCacheFilters(
    List<ParsedQueryParameter<OffsetDateTime>> eventCreatedDateTime,
    List<ParsedQueryParameter<OffsetDateTime>> eventDateTime,
    List<EventType> eventType,
    List<ShipmentEventTypeCode> shipmentEventTypeCode,
    List<TransportEventTypeCode> transportEventTypeCode,
    List<EquipmentEventTypeCode> equipmentEventTypeCode,
    List<DocumentTypeCode> documentTypeCode,
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
    public EventCacheFilters { }
  }

  public static Specification<EventCache> withFilters(final EventCacheFilters filters) {
    log.debug("Searching based on {}", filters);

    return (Root<EventCache> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      JsonPathExpressionBuilder jsonPath = new JsonPathExpressionBuilder(root, builder, EventCache_.CONTENT);
      EnumQueryTracker<EventType> eventTypeEnumQueryTracker = new EnumQueryTracker<>(EventType.class);
      SideEffect triggerTransportCallOnly = () -> eventTypeEnumQueryTracker.ensureEnumIsOneOf(EventType.ALL_EVENT_TYPES_WITH_TRANSPORT_CALL);
      Function<EventType, SideEffect> ensureEventTypeIsExactly = et -> () -> eventTypeEnumQueryTracker.ensureEnumIsExactly(et);

      if (filters.eventType != null) {
        eventTypeEnumQueryTracker.ensureEnumIsOneOf(filters.eventType);
      }

      conditionallyOneOfEnumFilter(
        predicates,
        jsonPath.of(TransportEvent_.TRANSPORT_EVENT_TYPE_CODE),
        filters.transportEventTypeCode,
        ensureEventTypeIsExactly.apply(EventType.TRANSPORT)
      );

      conditionallyOneOfEnumFilter(
        predicates,
        jsonPath.of(EquipmentEvent_.EQUIPMENT_EVENT_TYPE_CODE),
        filters.equipmentEventTypeCode,
        ensureEventTypeIsExactly.apply(EventType.EQUIPMENT)
      );


      conditionallyOneOfEnumFilter(
        predicates,
        jsonPath.of(ShipmentEvent_.SHIPMENT_EVENT_TYPE_CODE),
        filters.shipmentEventTypeCode,
        ensureEventTypeIsExactly.apply(EventType.SHIPMENT)
      );

      conditionallyOneOfEnumFilter(
        predicates,
        jsonPath.of(ShipmentEvent_.DOCUMENT_TYPE_CODE),
        filters.documentTypeCode,
        ensureEventTypeIsExactly.apply(EventType.SHIPMENT)
      );

      conditionallyAddEqualsFilter(
        predicates,
        builder,
        // Applies to more than just TransportEvent despite the TransportEvent_
        jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.VESSEL, Vessel_.VESSEL_IM_ONUMBER),
        filters.vesselIMONumber,
        triggerTransportCallOnly
      );

      conditionallyAddEqualsFilter(
        predicates,
        builder,
        // Applies to more than just TransportEvent despite the TransportEvent_
        jsonPath.of(TransportEvent_.TRANSPORT_CALL, BaseTransportCall_.TRANSPORT_CALL_REFERENCE),
        filters.transportCallReference,
        triggerTransportCallOnly
      );

      conditionallyAddEqualsFilter(
        predicates,
        builder,
        // Applies to more than just TransportEvent despite the TransportEvent_
        jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.EXPORT_VOYAGE, Voyage_.CARRIER_VOYAGE_NUMBER),
        filters.carrierExportVoyageNumber,
        triggerTransportCallOnly
      );

      conditionallyAddEqualsFilter(
        predicates,
        builder,
        // Applies to more than just TransportEvent despite the TransportEvent_
        jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.EXPORT_VOYAGE, Voyage_.UNIVERSAL_VOYAGE_REFERENCE),
        filters.universalExportVoyageReference,
        triggerTransportCallOnly
      );

      conditionallyAddEqualsFilter(
        predicates,
        builder,
        // Applies to more than just TransportEvent despite the TransportEvent_
        Arrays.asList(
          jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.EXPORT_VOYAGE, Voyage_.SERVICE, Service_.CARRIER_SERVICE_CODE),
          jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.IMPORT_VOYAGE, Voyage_.SERVICE, Service_.CARRIER_SERVICE_CODE)
        ),
        filters.carrierServiceCode,
        triggerTransportCallOnly
      );

      conditionallyAddEqualsFilter(
        predicates,
        builder,
        // Applies to more than just TransportEvent despite the TransportEvent_
        Arrays.asList(
          jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.EXPORT_VOYAGE, Voyage_.SERVICE, Service_.UNIVERSAL_SERVICE_REFERENCE),
          jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.IMPORT_VOYAGE, Voyage_.SERVICE, Service_.UNIVERSAL_SERVICE_REFERENCE)
        ),
        filters.universalServiceReference,
        triggerTransportCallOnly
      );

      conditionallyAddEqualsFilter(
        predicates,
        builder,
        // Applies to more than just TransportEvent despite the TransportEvent_
        jsonPath.of(TransportEvent_.TRANSPORT_CALL, BaseTransportCall_.LOCATION, Location_.U_NLOCATION_CODE),
        filters.UNLocationCode,
        triggerTransportCallOnly
      );

      if (filters.equipmentReference != null) {
        predicates.add(
          builder.or(
            builder.equal(
              jsonPath.of(EquipmentEvent_.EQUIPMENT, Equipment_.EQUIPMENT_REFERENCE),
              builder.literal(filters.equipmentReference)
            ),
            builder.like(root.get(EventCache_.REFERENCES), "%|CA=" + filters.equipmentReference+ "|%")
          )
        );
      }

      handleParsedQueryParameter(
        predicates,
        builder,
        root.get(Event_.EVENT_CREATED_DATE_TIME),
        filters.eventCreatedDateTime
      );

      handleParsedQueryParameter(
        predicates,
        builder,
        root.get(Event_.EVENT_DATE_TIME),
        filters.eventDateTime
      );

      if (filters.documentReference != null) {
        predicates.add(
          builder.like(root.get(EventCache_.DOCUMENT_REFERENCES), "%|%=" + filters.documentReference+ "|%")
        );
      }
      eventTypeEnumQueryTracker.apply(predicates, builder, root.get(EventCache_.EVENT_TYPE));
      return builder.and(predicates.toArray(Predicate[]::new));
    };
  }

  private static <E extends Enum<E>> void conditionallyOneOfEnumFilter(List<Predicate> predicates,
                                                                       Expression<?> field,
                                                                       List<E> values,
                                                                       SideEffect sideEffect) {
    if (values != null && !values.isEmpty()) {
      sideEffect.perform();
      predicates.add(field.in(values.stream().map(Enum::name).toList()));
    }
  }

  private static <T> void conditionallyAddEqualsFilter(List<Predicate> predicates,
                                                       CriteriaBuilder builder,
                                                       Expression<?> field,
                                                       T filterValue,
                                                       SideEffect sideEffect) {
    if (filterValue != null) {
      sideEffect.perform();
      predicates.add(
        builder.equal(
          field,
          filterValue
        )
      );
    }
  }

  private static <T> void conditionallyAddEqualsFilter(List<Predicate> predicates,
                                                       CriteriaBuilder builder,
                                                       Collection<Expression<?>> fields,
                                                       T filterValue,
                                                       SideEffect sideEffect) {
    if (filterValue != null) {
      sideEffect.perform();
      predicates.add(builder.or(
        fields.stream()
          .map(f -> builder.equal(f, filterValue))
          .toArray(Predicate[]::new))
      );
    }
  }

  private static <T extends Comparable<T>> void handleParsedQueryParameter(
    List<Predicate> predicates,
    CriteriaBuilder builder,
    Expression<T> field,
    List<ParsedQueryParameter<T>> filterValues
  ) {
    if (filterValues!= null && !filterValues.isEmpty()) {
      predicates.add(builder.or(filterValues.stream()
        .map(pqp -> processParsedQueryParameter(builder, field, pqp))
        .toArray(Predicate[]::new)
      ));
    }
  }

  private static <T extends Comparable<T>> Predicate processParsedQueryParameter(CriteriaBuilder builder,
                                                                                 Expression<T> field,
                                                                                 ParsedQueryParameter<T> parsedQueryParameter) {
    final T value = parsedQueryParameter.value();
    return switch (parsedQueryParameter.comparisonType()) {
      case EQ -> builder.equal(field, value);
      case GTE -> builder.greaterThanOrEqualTo(field, value);
      case GT -> builder.greaterThan(field, value);
      case LTE -> builder.lessThanOrEqualTo(field, value);
      case LT -> builder.lessThan(field, value);
    };
  }

  private static class EnumQueryTracker<E extends Enum<E>> {
    private final Class<E> clazz;
    private final EnumSet<E> exactlyEquals;

    private EnumSet<E> anyOf;

    EnumQueryTracker(Class<E> clazz) {
      this.clazz = clazz;
      exactlyEquals = EnumSet.noneOf(clazz);
    }

    void ensureEnumIsExactly(E e) {
      exactlyEquals.add(e);
    }

    void ensureEnumIsOneOf(Collection<E> values) {
      if (values == null || values.isEmpty()) {
        throw new IllegalArgumentException("values must be not null and non-empty");
      }
      if (anyOf == null) {
        anyOf = EnumSet.copyOf(values);
      } else {
        anyOf.retainAll(values);
      }
    }

    public void apply(List<Predicate> predicates, CriteriaBuilder builder, Expression<E> field) {
      if (anyOf != null) {
        if (anyOf.isEmpty()) {
          // anyOf.isEmpty can only be true if there is at least two different enum values and
          // ensureEnumIsOneOf is called twice with different with non-overlapping values.
          // This means we should return no matches, which we do by forcing a contradicting
          // (leverating exactlyEquals).
          exactlyEquals.addAll(EnumSet.allOf(clazz));
        } else {
          predicates.add(field.in(anyOf));
        }
      }
      for (E e : exactlyEquals) {
        // By design, if there are 2 or more entries in exactlyEquals, we trigger a
        // contradiction (meaning no matches).  This happens if you search for two
        // fields that are in two conflicting event types.
        predicates.add(builder.equal(field, e));
      }
    }
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

  @FunctionalInterface
  private interface SideEffect {
    void perform();
  }
}
