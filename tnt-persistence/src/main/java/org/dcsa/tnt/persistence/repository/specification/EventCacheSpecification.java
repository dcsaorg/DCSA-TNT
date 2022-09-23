package org.dcsa.tnt.persistence.repository.specification;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.dcsa.tnt.persistence.entity.EventCache;
import org.dcsa.tnt.persistence.entity.EventCache_;
import org.dcsa.tnt.persistence.entity.TransportCall;
import org.dcsa.tnt.persistence.entity.TransportCall_;
import org.dcsa.tnt.persistence.entity.TransportEvent;
import org.dcsa.tnt.persistence.entity.TransportEvent_;
import org.dcsa.tnt.persistence.entity.Vessel_;
import org.dcsa.tnt.persistence.entity.enums.EventType;
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
    String shipmentEventTypeCode,
    String carrierBookingReference,
    String transportDocumentReference,
    String transportDocumentTypeCode,
    String transportEventTypeCode,
    String transportCallID,
    String vesselIMONumber,
    String carrierVoyageNumber,
    String carrierServiceCode,
    String equipmentEventTypeCode,
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

      if (filters.vesselIMONumber != null) {
        predicates.add(
          builder.equal(
            jsonPath.of(TransportEvent_.TRANSPORT_CALL, TransportCall_.VESSEL, Vessel_.VESSEL_IM_ONUMBER),
            builder.literal(filters.vesselIMONumber)
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
    private final String field;

    public Expression<String> of(String... jsonPathElements) {
      List<Expression<?>> expressions = new ArrayList<>();
      expressions.add(root.get(field));
      for (String e : jsonPathElements) {
        expressions.add(builder.literal(e));
      }
      return builder.function("jsonb_extract_path_text", String.class, expressions.toArray(Expression[]::new));
    }
  }
}
