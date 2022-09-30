package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.pagination.Cursor;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.dcsa.tnt.persistence.entity.EventSubscription;
import org.dcsa.tnt.persistence.entity.EventSubscription.EventSubscriptionEnumSetItem;
import org.dcsa.tnt.persistence.entity.EventSubscriptionDocumentTypeCode;
import org.dcsa.tnt.persistence.entity.EventSubscriptionEquipmentEventTypeCode;
import org.dcsa.tnt.persistence.entity.EventSubscriptionEventType;
import org.dcsa.tnt.persistence.entity.EventSubscriptionShipmentEventTypeCode;
import org.dcsa.tnt.persistence.entity.EventSubscriptionTransportEventTypeCode;
import org.dcsa.tnt.persistence.repository.EventSubscriptionRepository;
import org.dcsa.tnt.service.mapping.EventSubscriptionMapper;
import org.dcsa.tnt.transferobjects.EventSubscriptionSecretTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionWithIdTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionWithSecretTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventSubscriptionService {
  private final EventSubscriptionRepository eventSubscriptionRepository;
  private final EventSubscriptionMapper eventSubscriptionMapper;

  @Transactional
  public PagedResult<EventSubscriptionWithIdTO> findAll(final Cursor cursor) {
    return new PagedResult<>(
      eventSubscriptionRepository.findAll(cursor.toPageRequest()),
      eventSubscriptionMapper::toDTO
    );
  }

  @Transactional
  public EventSubscriptionWithIdTO getSubscription(UUID subscriptionID) {
    return eventSubscriptionRepository.findById(subscriptionID)
      .map(eventSubscriptionMapper::toDTO)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No event-subscription found with id = " + subscriptionID));
  }

  @Transactional
  public EventSubscriptionWithIdTO createSubscription(EventSubscriptionWithSecretTO eventSubscriptionTo) {
    EventSubscription eventSubscription =
      eventSubscriptionRepository.save(
        eventSubscriptionMapper.toDAO(eventSubscriptionTo).toBuilder()
          .createdDateTime(OffsetDateTime.now())
          .build()
      );
    UUID subscriptionID = eventSubscription.getSubscriptionID();

    EventSubscription.EventSubscriptionBuilder builder = eventSubscription.toBuilder();
    setIfNotEmpty(subscriptionID, eventSubscriptionTo.getEventTypes(), eventSubscriptionMapper::toDAO, EventSubscriptionEventType::new, builder::eventTypes);
    setIfNotEmpty(subscriptionID, eventSubscriptionTo.getTransportEventTypeCodes(), eventSubscriptionMapper::toDAO, EventSubscriptionTransportEventTypeCode::new, builder::transportEventTypeCodes);
    setIfNotEmpty(subscriptionID, eventSubscriptionTo.getShipmentEventTypeCodes(), eventSubscriptionMapper::toDAO, EventSubscriptionShipmentEventTypeCode::new, builder::shipmentEventTypeCodes);
    setIfNotEmpty(subscriptionID, eventSubscriptionTo.getEquipmentEventTypeCodes(), eventSubscriptionMapper::toDAO, EventSubscriptionEquipmentEventTypeCode::new, builder::equipmentEventTypeCodes);
    setIfNotEmpty(subscriptionID, eventSubscriptionTo.getDocumentTypeCodes(), eventSubscriptionMapper::toDAO, EventSubscriptionDocumentTypeCode::new, builder::documentTypeCodes);

    eventSubscriptionRepository.save(builder.build());

    return getSubscription(subscriptionID);
  }

  @Transactional
  public void deleteSubscription(UUID subscriptionID) {
    eventSubscriptionRepository.deleteById(subscriptionID);
  }

  @Transactional
  public void updateSubscription(UUID subscriptionID, EventSubscriptionTO eventSubscription) {
    EventSubscription original = eventSubscriptionRepository.findById(subscriptionID)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No event-subscription found with id = " + subscriptionID));

    updateList(subscriptionID, eventSubscription.getEventTypes(), eventSubscriptionMapper::toDAO, EventSubscriptionEventType::new, eventSubscriptionRepository::deleteEventType, original.getEventTypes());
    updateList(subscriptionID, eventSubscription.getTransportEventTypeCodes(), eventSubscriptionMapper::toDAO, EventSubscriptionTransportEventTypeCode::new, eventSubscriptionRepository::deleteTransportEventTypeCode, original.getTransportEventTypeCodes());
    updateList(subscriptionID, eventSubscription.getShipmentEventTypeCodes(), eventSubscriptionMapper::toDAO, EventSubscriptionShipmentEventTypeCode::new, eventSubscriptionRepository::deleteShipmentEventTypeCode, original.getShipmentEventTypeCodes());
    updateList(subscriptionID, eventSubscription.getEquipmentEventTypeCodes(), eventSubscriptionMapper::toDAO, EventSubscriptionEquipmentEventTypeCode::new, eventSubscriptionRepository::deleteEquipmentEventTypeCode, original.getEquipmentEventTypeCodes());
    updateList(subscriptionID, eventSubscription.getDocumentTypeCodes(), eventSubscriptionMapper::toDAO, EventSubscriptionDocumentTypeCode::new, eventSubscriptionRepository::deleteDocumentTypeCode, original.getDocumentTypeCodes());

    eventSubscriptionRepository.save(
      original.toBuilder()
        .callbackUrl(eventSubscription.getCallbackUrl())
        .documentReference(eventSubscription.getDocumentReference())
        .equipmentReference(eventSubscription.getEquipmentReference())
        .transportCallReference(eventSubscription.getTransportCallReference())
        .vesselIMONumber(eventSubscription.getVesselIMONumber())
        .carrierExportVoyageNumber(eventSubscription.getCarrierExportVoyageNumber())
        .universalExportVoyageReference(eventSubscription.getUniversalExportVoyageReference())
        .carrierServiceCode(eventSubscription.getCarrierServiceCode())
        .universalServiceReference(eventSubscription.getUniversalServiceReference())
        .UNLocationCode(eventSubscription.getUNLocationCode())
        .build()
    );
  }

  @Transactional
  public void updateSecret(UUID subscriptionID, EventSubscriptionSecretTO eventSubscriptionSecret) {
    eventSubscriptionRepository.updateSecret(subscriptionID, eventSubscriptionSecret.secret());
  }

  private <S extends Enum<S>, T extends Enum<T>, TI extends EventSubscriptionEnumSetItem<T>> void setIfNotEmpty(
    UUID subscriptionId,
    Set<S> src,
    Function<S, T> enumMapper,
    BiFunction<UUID, T, TI> entityFactory,
    Consumer<Set<TI>> target
  ) {
    if (src != null) {
      target.accept(src.stream()
        .map(enumMapper)
        .map(e -> entityFactory.apply(subscriptionId, e))
        .collect(Collectors.toSet()));
    }
  }

  private <S extends Enum<S>, T extends Enum<T>, TI extends EventSubscriptionEnumSetItem<T>> void updateList(
    UUID subscriptionId,
    Set<S> newSet,
    Function<S, T> enumMapper,
    BiFunction<UUID, T, TI> entityFactory,
    BiConsumer<UUID, T> deleter,
    Set<TI> targetSet
  ) {
    if (newSet == null) {
      newSet = Collections.emptySet();
    }
    Set<T> mappedSet = newSet.stream().map(enumMapper).collect(Collectors.toSet());

    Iterator<TI> it = targetSet.iterator();
    while (it.hasNext()) {
      TI item = it.next();
      if (!mappedSet.remove(item.getValue())) {
        it.remove();
        deleter.accept(subscriptionId, item.getValue());
      }
    }

    mappedSet.forEach(e -> targetSet.add(entityFactory.apply(subscriptionId, e)));
  }
}
