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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TNTEventSubscriptionTOServiceImpl
    extends EventSubscriptionTOServiceImpl<TNTEventSubscriptionTO, EventSubscriptionService, EventSubscriptionRepository> {

  private final EventSubscriptionService eventSubscriptionService;
  private final EventSubscriptionRepository eventSubscriptionRepository;

  @Override
  protected EventSubscriptionService getService() {
    return this.eventSubscriptionService;
  }

    @Override
    protected EventSubscriptionRepository getRepository() {
        return this.eventSubscriptionRepository;
    }

    @Override
  protected List<EventType> getAllowedEventTypes() {
    return List.of(EventType.SHIPMENT, EventType.TRANSPORT, EventType.EQUIPMENT);
  }

  // ToDo : replace this with mapstruct
  @Override
  protected Function<TNTEventSubscriptionTO, EventSubscription>
      eventSubscriptionTOToEventSubscription() {
    return esTo -> {
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
  }

  // ToDo : replace this with mapstruct
  protected Function<EventSubscription, TNTEventSubscriptionTO>
      eventSubscriptionToEventSubscriptionTo() {
    return es -> {
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

  @Override
  protected List<EventType> getEventTypesForTo(TNTEventSubscriptionTO eventSubscriptionTO) {

    if (null == eventSubscriptionTO.getEventType()
        || eventSubscriptionTO.getEventType().isEmpty()) {
      eventSubscriptionTO.setEventType(getAllowedEventTypes());
    }

    return eventSubscriptionTO.getEventType();
  }

  @Override
  protected List<TransportDocumentTypeCode> getTransportDocumentTypesForTo(
      TNTEventSubscriptionTO eventSubscriptionTO) {

    if (null == eventSubscriptionTO.getTransportDocumentTypeCode()
        || eventSubscriptionTO.getTransportDocumentTypeCode().isEmpty()) {
      eventSubscriptionTO.setTransportDocumentTypeCode(ALL_TRANSPORT_DOCUMENT_TYPES);
    }

    return eventSubscriptionTO.getTransportDocumentTypeCode();
  }

  @Override
  protected List<ShipmentEventTypeCode> getShipmentEventTypeCodesForTo(
      TNTEventSubscriptionTO eventSubscriptionTO) {

    if (null == eventSubscriptionTO.getShipmentEventTypeCode()
        || eventSubscriptionTO.getShipmentEventTypeCode().isEmpty()) {
      eventSubscriptionTO.setShipmentEventTypeCode(ALL_SHIPMENT_EVENT_TYPES);
    }

    return eventSubscriptionTO.getShipmentEventTypeCode();
  }

  @Override
  protected List<TransportEventTypeCode> getTransportEventTypeCodesForTo(
      TNTEventSubscriptionTO eventSubscriptionTO) {

    if (null == eventSubscriptionTO.getTransportEventTypeCode()
        || eventSubscriptionTO.getTransportEventTypeCode().isEmpty()) {
      eventSubscriptionTO.setTransportEventTypeCode(ALL_TRANSPORT_EVENT_TYPES);
    }

    return eventSubscriptionTO.getTransportEventTypeCode();
  }

  @Override
  protected List<EquipmentEventTypeCode> getEquipmentEventTypeCodesForTo(
      TNTEventSubscriptionTO eventSubscriptionTO) {

    if (null == eventSubscriptionTO.getEquipmentEventTypeCode()
        || eventSubscriptionTO.getEquipmentEventTypeCode().isEmpty()) {
      eventSubscriptionTO.setEquipmentEventTypeCode(ALL_EQUIPMENT_EVENT_TYPES);
    }

    return eventSubscriptionTO.getEquipmentEventTypeCode();
  }

  @Override
  protected List<OperationsEventTypeCode> getOperationsEventTypeCodesForTo(
      TNTEventSubscriptionTO eventSubscriptionTO) {
    // we don't need OperationsEventTypeCode for TNT event subscriptions
    throw new UnsupportedOperationException();
  }

  @Override
  public Mono<TNTEventSubscriptionTO> create(TNTEventSubscriptionTO eventSubscriptionTO) {
    return validateCreateRequest(eventSubscriptionTO)
        .then(
            eventSubscriptionService.create(
                eventSubscriptionTOToEventSubscription().apply(eventSubscriptionTO)))
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

  @Override
  public Mono<TNTEventSubscriptionTO> update(TNTEventSubscriptionTO eventSubscriptionTO) {
    return validateUpdateRequest(eventSubscriptionTO)
        .then(
            eventSubscriptionRepository.deleteEventTypesForSubscription(
                eventSubscriptionTO.getSubscriptionID()))
        .thenReturn(eventSubscriptionTO)
        .map(eventSubscriptionTOToEventSubscription())
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


  @Override
  protected Mono<TNTEventSubscriptionTO> mapSingleD2TO(
      Mono<EventSubscription> eventSubscriptionMono) {
    return eventSubscriptionMono
        .map(eventSubscriptionToEventSubscriptionTo())
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
        .map(eventSubscriptionToEventSubscriptionTo())
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

}
