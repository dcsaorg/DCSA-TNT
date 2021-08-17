package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.events.model.EventSubscription;
import org.dcsa.core.events.model.base.AbstractEventSubscription;
import org.dcsa.core.events.model.enums.EquipmentEventTypeCode;
import org.dcsa.core.events.model.enums.EventType;
import org.dcsa.core.events.model.enums.ShipmentEventTypeCode;
import org.dcsa.core.events.model.enums.TransportEventTypeCode;
import org.dcsa.core.events.repository.EventSubscriptionRepository;
import org.dcsa.core.events.service.EventSubscriptionService;
import org.dcsa.core.events.service.impl.EventSubscriptionTOServiceImpl;
import org.dcsa.core.exception.UpdateException;
import org.dcsa.core.util.MappingUtils;
import org.dcsa.tnt.model.transferobjects.TNTEventSubscriptionTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TNTEventSubscriptionTOServiceImpl
    extends EventSubscriptionTOServiceImpl<TNTEventSubscriptionTO, EventSubscriptionService> {

  private static final String ALL_ALLOWED_EVENT_TYPES =
      EventType.SHIPMENT.name()
          + ","
          + EventType.TRANSPORT.name()
          + ","
          + EventType.EQUIPMENT.name();

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
            eventSubscription.setTransportDocumentTypeCode(esTo.getTransportDocumentTypeCode());
            eventSubscription.setTransportCallID(esTo.getTransportCallID());
            return eventSubscription;
          };

  @Override
  public Mono<TNTEventSubscriptionTO> update(TNTEventSubscriptionTO eventSubscriptionTO) {
    if (eventSubscriptionTO.getSecret() != null) {
      return Mono.error(
          new UpdateException(
              "Please omit the \"secret\" attribute.  If you want to change the"
                  + " secret, please use the dedicated secret endpoint"
                  + " (\"PUT .../event-subscriptions/"
                  + eventSubscriptionTO.getSubscriptionID()
                  + "/secret\")."));
    }
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

    String eventTypes;

    if (!StringUtils.hasLength(eventSubscriptionTO.getEventType())) {
      eventTypes = ALL_ALLOWED_EVENT_TYPES;
      eventSubscriptionTO.setEventType(eventTypes);
    } else {
      eventTypes = eventSubscriptionTO.getEventType();
    }
    return Flux.fromIterable(stringToEventTypeList.apply(eventTypes))
        .concatMap(
            eventType ->
                eventSubscriptionRepository.insertEventTypeForSubscription(
                    eventSubscriptionTO.getSubscriptionID(), eventType))
        .then(Mono.just(eventSubscriptionTO));
  }


  private Mono<Void> createShipmentEventType(TNTEventSubscriptionTO eventSubscriptionTO) {
    String shipmentEventTypeCode = eventSubscriptionTO.getShipmentEventTypeCode();
    if (StringUtils.hasLength(shipmentEventTypeCode) && shipmentEventTypeCode.contains(",")) {
      return Flux.fromIterable(
              Arrays.stream(shipmentEventTypeCode.split(",")).collect(Collectors.toList()))
          .flatMap(
              s ->
                  eventSubscriptionRepository.insertShipmentEventTypeForSubscription(
                      eventSubscriptionTO.getSubscriptionID(), ShipmentEventTypeCode.valueOf(s)))
          .then();
    } else if (StringUtils.hasLength(shipmentEventTypeCode)) {
      return eventSubscriptionRepository.insertShipmentEventTypeForSubscription(
          eventSubscriptionTO.getSubscriptionID(),
          ShipmentEventTypeCode.valueOf(shipmentEventTypeCode));
    }
    return Mono.empty();
  }

  private Mono<Void> createTransportEventType(TNTEventSubscriptionTO eventSubscriptionTO) {
    String transportEventTypeCode = eventSubscriptionTO.getTransportEventTypeCode();
    if (StringUtils.hasLength(transportEventTypeCode) && transportEventTypeCode.contains(",")) {
      return Flux.fromIterable(
              Arrays.stream(transportEventTypeCode.split(",")).collect(Collectors.toList()))
          .flatMap(
              e ->
                  eventSubscriptionRepository.insertTransportEventTypeForSubscription(
                      eventSubscriptionTO.getSubscriptionID(), TransportEventTypeCode.valueOf(e)))
          .then();
    } else if (StringUtils.hasLength(transportEventTypeCode)) {
      return eventSubscriptionRepository.insertTransportEventTypeForSubscription(
          eventSubscriptionTO.getSubscriptionID(),
          TransportEventTypeCode.valueOf(transportEventTypeCode));
    }
    return Mono.empty();
  }

  private Mono<Void> createEquipmentEventType(TNTEventSubscriptionTO eventSubscriptionTO) {
    String equipmentEventTypeCode = eventSubscriptionTO.getEquipmentEventTypeCode();
    if (StringUtils.hasLength(equipmentEventTypeCode) && equipmentEventTypeCode.contains(",")) {
      return Flux.fromIterable(
              Arrays.stream(equipmentEventTypeCode.split(",")).collect(Collectors.toList()))
          .flatMap(
              s ->
                  eventSubscriptionRepository.insertEquipmentEventTypeForSubscription(
                      eventSubscriptionTO.getSubscriptionID(), EquipmentEventTypeCode.valueOf(s)))
          .then();
    } else if (StringUtils.hasLength(equipmentEventTypeCode)) {
      return eventSubscriptionRepository.insertEquipmentEventTypeForSubscription(
          eventSubscriptionTO.getSubscriptionID(),
          EquipmentEventTypeCode.valueOf(equipmentEventTypeCode));
    }
    return Mono.empty();
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
                    .map(event -> EventType.valueOf(event).name())
                    .collectList()
                    .doOnNext(events -> eventSubscriptionTO.setEventType(String.join(",", events)))
                    .thenReturn(eventSubscriptionTO))
        .flatMap(
            esTo ->
                eventSubscriptionRepository
                    .findShipmentEventTypesForSubscriptionID(esTo.getSubscriptionID())
                    .map(se -> ShipmentEventTypeCode.valueOf(se).name())
                    .collectList()
                    .doOnNext(events -> esTo.setShipmentEventTypeCode(String.join(",", events)))
                    .thenReturn(esTo))
        .flatMap(
            esTo ->
                eventSubscriptionRepository
                    .findTransportEventTypesForSubscriptionID(esTo.getSubscriptionID())
                    .map(se -> TransportEventTypeCode.valueOf(se).name())
                    .collectList()
                    .doOnNext(events -> esTo.setTransportEventTypeCode(String.join(",", events)))
                    .thenReturn(esTo))
        .flatMap(
            esTo ->
                eventSubscriptionRepository
                    .findEquipmentEventTypesForSubscriptionID(esTo.getSubscriptionID())
                    .map(se -> EquipmentEventTypeCode.valueOf(se).name())
                    .collectList()
                    .doOnNext(events -> esTo.setEquipmentEventTypeCode(String.join(",", events)))
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
                        if (!StringUtils.hasLength(subscriptionTO.getEventType())) {
                          subscriptionTO.setEventType(
                              eventSubscriptionEventType.getEventType().name());
                        } else if (StringUtils.hasLength(subscriptionTO.getEventType())
                            && !subscriptionTO.getEventType().endsWith(",")) {
                          subscriptionTO.setEventType(
                              subscriptionTO
                                  .getEventType()
                                  .concat(",")
                                  .concat(eventSubscriptionEventType.getEventType().name()));
                        }
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
                        if (!StringUtils.hasLength(subscriptionTO.getShipmentEventTypeCode())) {
                          subscriptionTO.setShipmentEventTypeCode(
                              esShipmentEventType.getShipmentEventTypeCode().name());
                        } else if (StringUtils.hasLength(subscriptionTO.getShipmentEventTypeCode())
                            && !subscriptionTO.getShipmentEventTypeCode().endsWith(",")) {
                          subscriptionTO.setShipmentEventTypeCode(
                              subscriptionTO
                                  .getShipmentEventTypeCode()
                                  .concat(",")
                                  .concat(esShipmentEventType.getShipmentEventTypeCode().name()));
                        }
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
                        if (!StringUtils.hasLength(subscriptionTO.getTransportEventTypeCode())) {
                          subscriptionTO.setTransportEventTypeCode(
                              esTransportEventType.getTransportEventTypeCode().name());
                        } else if (StringUtils.hasLength(subscriptionTO.getTransportEventTypeCode())
                            && !subscriptionTO.getTransportEventTypeCode().endsWith(",")) {
                          subscriptionTO.setTransportEventTypeCode(
                              subscriptionTO
                                  .getTransportEventTypeCode()
                                  .concat(",")
                                  .concat(esTransportEventType.getTransportEventTypeCode().name()));
                        }
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
                        if (!StringUtils.hasLength(subscriptionTO.getEquipmentEventTypeCode())) {
                          subscriptionTO.setEquipmentEventTypeCode(
                              esEquipmentEventType.getEquipmentEventTypeCode().name());
                        } else if (StringUtils.hasLength(subscriptionTO.getEquipmentEventTypeCode())
                            && !subscriptionTO.getEquipmentEventTypeCode().endsWith(",")) {
                          subscriptionTO.setEquipmentEventTypeCode(
                              subscriptionTO
                                  .getEquipmentEventTypeCode()
                                  .concat(",")
                                  .concat(esEquipmentEventType.getEquipmentEventTypeCode().name()));
                        }
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
            eventSubscriptionTo.setTransportDocumentTypeCode(es.getTransportDocumentTypeCode());
            eventSubscriptionTo.setTransportCallID(es.getTransportCallID());
            return eventSubscriptionTo;
          };
}
