package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.events.model.EventSubscription;
import org.dcsa.core.events.model.base.AbstractEventSubscription;
import org.dcsa.core.events.model.enums.*;
import org.dcsa.core.events.repository.EventSubscriptionRepository;
import org.dcsa.core.events.service.EventSubscriptionService;
import org.dcsa.core.events.service.impl.EventSubscriptionTOServiceImpl;
import org.dcsa.core.util.MappingUtils;
import org.dcsa.tnt.model.transferobjects.TNTEventSubscriptionTO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TNTEventSubscriptionTOServiceImpl
    extends EventSubscriptionTOServiceImpl<TNTEventSubscriptionTO, EventSubscriptionService> {

  private static final List<EventType> ALL_ALLOWED_EVENT_TYPES =
      List.of(EventType.SHIPMENT, EventType.TRANSPORT, EventType.EQUIPMENT);

  private static final List<TransportDocumentTypeCode> ALL_TRANSPORT_DOCUMENT_TYPES =
      List.of(TransportDocumentTypeCode.values());

  private static final List<ShipmentEventTypeCode> ALL_SHIPMENT_EVENT_TYPES =
      List.of(ShipmentEventTypeCode.values());

  private static final List<TransportEventTypeCode> ALL_TRANSPORT_EVENT_TYPES =
      List.of(TransportEventTypeCode.values());

  private static final List<EquipmentEventTypeCode> ALL_EQUIPMENT_EVENT_TYPES =
      List.of(EquipmentEventTypeCode.values());

  private final EventSubscriptionService eventSubscriptionService;
  private final EventSubscriptionRepository eventSubscriptionRepository;

  @Override
  protected EventSubscriptionService getService() {
    return this.eventSubscriptionService;
  }

  @Override
  public Mono<TNTEventSubscriptionTO> create(TNTEventSubscriptionTO eventSubscriptionTO) {
    return eventSubscriptionService
        .create(eventSubscriptionTOToEventSubscription.apply(eventSubscriptionTO))
        .flatMap(
            eventSubscription -> {
              eventSubscriptionTO.setSubscriptionID(eventSubscription.getSubscriptionID());
              return createEventTypes(eventSubscriptionTO)
                  .then(createTransportDocumentEventTypes(eventSubscriptionTO))
                  .then(createShipmentEventType(eventSubscriptionTO))
                  .then(createTransportEventType(eventSubscriptionTO))
                  .then(createEquipmentEventType(eventSubscriptionTO))
                  .thenReturn(eventSubscriptionTO);
            });
  }

  // ToDo : replace this with mapstruct
  private final Function<TNTEventSubscriptionTO, EventSubscription>
      eventSubscriptionTOToEventSubscription =
          esTo -> {
            EventSubscription eventSubscription = new EventSubscription();
            eventSubscription.setSubscriptionID(esTo.getSubscriptionID());
            eventSubscription.setCallbackUrl(esTo.getCallbackUrl());
            eventSubscription.setSecret(esTo.getSecret());
            eventSubscription.setCarrierBookingReference(esTo.getCarrierBookingReference());
            eventSubscription.setEquipmentReference(esTo.getEquipmentReference());
            eventSubscription.setCarrierServiceCode(esTo.getCarrierServiceCode());
            eventSubscription.setCarrierVoyageNumber(esTo.getCarrierVoyageNumber());
            eventSubscription.setVesselIMONumber(esTo.getVesselIMONumber());
            eventSubscription.setTransportDocumentReference(esTo.getTransportDocumentReference());
            eventSubscription.setTransportCallID(esTo.getTransportCallID());
            return eventSubscription;
          };

  @Override
  public Mono<TNTEventSubscriptionTO> update(TNTEventSubscriptionTO eventSubscriptionTO) {
    return eventSubscriptionRepository
        .deleteEventTypesForSubscription(eventSubscriptionTO.getSubscriptionID())
        .thenReturn(eventSubscriptionTO)
        .map(eventSubscriptionTOToEventSubscription)
        .flatMap(
            updated ->
                eventSubscriptionService
                    .findById(updated.getSubscriptionID())
                    .map(
                        original -> {
                          updated.setSecret(original.getSecret());
                          updated.copyInternalFieldsFrom(original);
                          return updated;
                        }))
        .flatMap(eventSubscriptionService::update)
        .flatMap(ignored -> createEventTypes(eventSubscriptionTO));
  }

  private Mono<TNTEventSubscriptionTO> createEventTypes(
      TNTEventSubscriptionTO eventSubscriptionTO) {

    List<EventType> eventTypes;

    if (CollectionUtils.isEmpty(eventSubscriptionTO.getEventType())) {
      eventTypes = ALL_ALLOWED_EVENT_TYPES;
      eventSubscriptionTO.setEventType(eventTypes);
    } else {
      eventTypes = eventSubscriptionTO.getEventType();
    }

    return Flux.fromIterable(eventTypes)
        .concatMap(
            eventType ->
                eventSubscriptionRepository.insertEventTypeForSubscription(
                    eventSubscriptionTO.getSubscriptionID(), eventType))
        .then(Mono.just(eventSubscriptionTO));
  }

  private Mono<Void> createTransportDocumentEventTypes(TNTEventSubscriptionTO eventSubscriptionTO) {

    if (null == eventSubscriptionTO.getTransportDocumentTypeCode()) {
      eventSubscriptionTO.setTransportDocumentTypeCode(ALL_TRANSPORT_DOCUMENT_TYPES);
    }

    List<TransportDocumentTypeCode> transportDocumentTypeCodes =
        eventSubscriptionTO.getTransportDocumentTypeCode();
    return Flux.fromIterable(transportDocumentTypeCodes)
        .flatMap(
            td ->
                eventSubscriptionRepository.insertTransportDocumentEventTypeForSubscription(
                    eventSubscriptionTO.getSubscriptionID(), td))
        .then();
  }

  private Mono<Void> createShipmentEventType(TNTEventSubscriptionTO eventSubscriptionTO) {

    if (null == eventSubscriptionTO.getShipmentEventTypeCode()) {
      eventSubscriptionTO.setShipmentEventTypeCode(ALL_SHIPMENT_EVENT_TYPES);
    }

    List<ShipmentEventTypeCode> shipmentEventTypeCode =
        eventSubscriptionTO.getShipmentEventTypeCode();

    return Flux.fromIterable(shipmentEventTypeCode)
        .flatMap(
            s ->
                eventSubscriptionRepository.insertShipmentEventTypeForSubscription(
                    eventSubscriptionTO.getSubscriptionID(), s))
        .then();
  }

  private Mono<Void> createTransportEventType(TNTEventSubscriptionTO eventSubscriptionTO) {

    if (null == eventSubscriptionTO.getTransportEventTypeCode()) {
      eventSubscriptionTO.setTransportEventTypeCode(ALL_TRANSPORT_EVENT_TYPES);
    }

    List<TransportEventTypeCode> transportEventTypeCode =
        eventSubscriptionTO.getTransportEventTypeCode();
    return Flux.fromIterable(transportEventTypeCode)
        .flatMap(
            t ->
                eventSubscriptionRepository.insertTransportEventTypeForSubscription(
                    eventSubscriptionTO.getSubscriptionID(), t))
        .then();
  }

  private Mono<Void> createEquipmentEventType(TNTEventSubscriptionTO eventSubscriptionTO) {

    if (null == eventSubscriptionTO.getEquipmentEventTypeCode()) {
      eventSubscriptionTO.setEquipmentEventTypeCode(ALL_EQUIPMENT_EVENT_TYPES);
    }

    List<EquipmentEventTypeCode> equipmentEventTypeCode =
        eventSubscriptionTO.getEquipmentEventTypeCode();

    return Flux.fromIterable(equipmentEventTypeCode)
        .flatMap(
            e ->
                eventSubscriptionRepository.insertEquipmentEventTypeForSubscription(
                    eventSubscriptionTO.getSubscriptionID(), e))
        .then();
  }

  @Override
  protected Mono<TNTEventSubscriptionTO> mapSingleD2TO(
      Mono<EventSubscription> eventSubscriptionMono) {
    return eventSubscriptionMono
        .map(eventSubscriptionToEventSubscriptionTo)
        .flatMap(
            eventSubscriptionTO ->
                eventSubscriptionRepository
                    .findEventTypesForSubscription(eventSubscriptionTO.getSubscriptionID())
                    .map(EventType::valueOf)
                    .collectList()
                    .doOnNext(eventSubscriptionTO::setEventType)
                    .thenReturn(eventSubscriptionTO))
        .flatMap(
            esTo ->
                eventSubscriptionRepository
                    .findTransportDocumentEventTypesForSubscription(esTo.getSubscriptionID())
                    .map(TransportDocumentTypeCode::valueOf)
                    .collectList()
                    .doOnNext(esTo::setTransportDocumentTypeCode)
                    .thenReturn(esTo))
        .flatMap(
            esTo ->
                eventSubscriptionRepository
                    .findShipmentEventTypesForSubscriptionID(esTo.getSubscriptionID())
                    .map(ShipmentEventTypeCode::valueOf)
                    .collectList()
                    .doOnNext(esTo::setShipmentEventTypeCode)
                    .thenReturn(esTo))
        .flatMap(
            esTo ->
                eventSubscriptionRepository
                    .findTransportEventTypesForSubscriptionID(esTo.getSubscriptionID())
                    .map(TransportEventTypeCode::valueOf)
                    .collectList()
                    .doOnNext(esTo::setTransportEventTypeCode)
                    .thenReturn(esTo))
        .flatMap(
            esTo ->
                eventSubscriptionRepository
                    .findEquipmentEventTypesForSubscriptionID(esTo.getSubscriptionID())
                    .map(EquipmentEventTypeCode::valueOf)
                    .collectList()
                    .doOnNext(esTo::setEquipmentEventTypeCode)
                    .thenReturn(esTo));
  }

  @Override
  protected Flux<TNTEventSubscriptionTO> mapManyD2TO(
      Flux<EventSubscription> eventSubscriptionFlux) {
    return eventSubscriptionFlux
        .map(eventSubscriptionToEventSubscriptionTo)
        .collectList()
        .flatMapMany(
            eventSubscriptionList -> {
              Map<UUID, TNTEventSubscriptionTO> id2subscription =
                  eventSubscriptionList.stream()
                      .collect(
                          Collectors.toMap(
                              AbstractEventSubscription::getSubscriptionID, Function.identity()));
              return Flux.fromIterable(eventSubscriptionList)
                  .map(AbstractEventSubscription::getSubscriptionID)
                  .buffer(MappingUtils.SQL_LIST_BUFFER_SIZE)
                  .concatMap(eventSubscriptionRepository::findEventTypesForSubscriptionIDIn)
                  .doOnNext(
                      eventSubscriptionEventType -> {
                        TNTEventSubscriptionTO subscriptionTO =
                            id2subscription.get(eventSubscriptionEventType.getSubscriptionID());
                        assert subscriptionTO != null;
                        if (CollectionUtils.isEmpty(subscriptionTO.getEventType())) {
                          subscriptionTO.setEventType(new ArrayList<>());
                        }
                        subscriptionTO
                            .getEventType()
                            .add(eventSubscriptionEventType.getEventType());
                      })
                  .thenMany(Flux.fromIterable(eventSubscriptionList))
                  .map(AbstractEventSubscription::getSubscriptionID)
                  .buffer(MappingUtils.SQL_LIST_BUFFER_SIZE)
                  .concatMap(
                      eventSubscriptionRepository
                          ::findTransportDocumentEventTypesForSubscriptionIDIn)
                  .doOnNext(
                      esTransportDocumentEventType -> {
                        TNTEventSubscriptionTO subscriptionTO =
                            id2subscription.get(esTransportDocumentEventType.getSubscriptionID());
                        assert subscriptionTO != null;
                        if (CollectionUtils.isEmpty(
                            subscriptionTO.getTransportDocumentTypeCode())) {
                          subscriptionTO.setTransportDocumentTypeCode(new ArrayList<>());
                        }
                        subscriptionTO
                            .getTransportDocumentTypeCode()
                            .add(esTransportDocumentEventType.getTransportDocumentTypeCode());
                      })
                  .thenMany(Flux.fromIterable(eventSubscriptionList))
                  .map(AbstractEventSubscription::getSubscriptionID)
                  .buffer(MappingUtils.SQL_LIST_BUFFER_SIZE)
                  .concatMap(eventSubscriptionRepository::findShipmentEventTypesForSubscriptionIDIn)
                  .doOnNext(
                      esShipmentEventType -> {
                        TNTEventSubscriptionTO subscriptionTO =
                            id2subscription.get(esShipmentEventType.getSubscriptionID());
                        assert subscriptionTO != null;
                        if (CollectionUtils.isEmpty(subscriptionTO.getShipmentEventTypeCode())) {
                          subscriptionTO.setShipmentEventTypeCode(new ArrayList<>());
                        }
                        subscriptionTO
                            .getShipmentEventTypeCode()
                            .add(esShipmentEventType.getShipmentEventTypeCode());
                      })
                  .thenMany(Flux.fromIterable(eventSubscriptionList))
                  .map(AbstractEventSubscription::getSubscriptionID)
                  .buffer(MappingUtils.SQL_LIST_BUFFER_SIZE)
                  .concatMap(
                      eventSubscriptionRepository::findTransportEventTypesForSubscriptionIDIn)
                  .doOnNext(
                      esTransportEventType -> {
                        TNTEventSubscriptionTO subscriptionTO =
                            id2subscription.get(esTransportEventType.getSubscriptionID());
                        assert subscriptionTO != null;
                        if (CollectionUtils.isEmpty(subscriptionTO.getTransportEventTypeCode())) {
                          subscriptionTO.setTransportEventTypeCode(new ArrayList<>());
                        }
                        subscriptionTO
                            .getTransportEventTypeCode()
                            .add(esTransportEventType.getTransportEventTypeCode());
                      })
                  .thenMany(Flux.fromIterable(eventSubscriptionList))
                  .map(AbstractEventSubscription::getSubscriptionID)
                  .buffer(MappingUtils.SQL_LIST_BUFFER_SIZE)
                  .concatMap(
                      eventSubscriptionRepository::findEquipmentEventTypesForSubscriptionIDIn)
                  .doOnNext(
                      esEquipmentEventType -> {
                        TNTEventSubscriptionTO subscriptionTO =
                            id2subscription.get(esEquipmentEventType.getSubscriptionID());
                        assert subscriptionTO != null;
                        if (CollectionUtils.isEmpty(subscriptionTO.getEquipmentEventTypeCode())) {
                          subscriptionTO.setEquipmentEventTypeCode(new ArrayList<>());
                        }
                        subscriptionTO
                            .getEquipmentEventTypeCode()
                            .add(esEquipmentEventType.getEquipmentEventTypeCode());
                      })
                  .thenMany(Flux.fromIterable(eventSubscriptionList));
            });
  }

  // ToDo : replace this with mapstruct
  private final Function<EventSubscription, TNTEventSubscriptionTO>
      eventSubscriptionToEventSubscriptionTo =
          es -> {
            TNTEventSubscriptionTO eventSubscriptionTo = new TNTEventSubscriptionTO();
            eventSubscriptionTo.setSubscriptionID(es.getSubscriptionID());
            eventSubscriptionTo.setCallbackUrl(es.getCallbackUrl());
            eventSubscriptionTo.setCarrierBookingReference(es.getCarrierBookingReference());
            eventSubscriptionTo.setEquipmentReference(es.getEquipmentReference());
            eventSubscriptionTo.setCarrierServiceCode(es.getCarrierServiceCode());
            eventSubscriptionTo.setCarrierVoyageNumber(es.getCarrierVoyageNumber());
            eventSubscriptionTo.setVesselIMONumber(es.getVesselIMONumber());
            eventSubscriptionTo.setTransportDocumentReference(es.getTransportDocumentReference());
            eventSubscriptionTo.setTransportCallID(es.getTransportCallID());
            return eventSubscriptionTo;
          };
}
