package org.dcsa.tnt.service.impl;

import lombok.RequiredArgsConstructor;
import org.dcsa.core.events.model.EquipmentEvent;
import org.dcsa.core.events.model.Event;
import org.dcsa.core.events.model.ShipmentEvent;
import org.dcsa.core.events.model.TransportEvent;
import org.dcsa.core.events.model.enums.DocumentReferenceType;
import org.dcsa.core.events.model.enums.TransportEventTypeCode;
import org.dcsa.core.events.model.transferobjects.DocumentReferenceTO;
import org.dcsa.core.events.repository.TransportCallRepository;
import org.dcsa.core.exception.CreateException;
import org.dcsa.core.exception.UpdateException;
import org.dcsa.core.service.impl.ExtendedBaseServiceImpl;
import org.dcsa.core.util.ValidationUtils;
import org.dcsa.tnt.model.EventSubscription;
import org.dcsa.core.events.model.enums.SignatureMethod;
import org.dcsa.tnt.repository.EventSubscriptionRepository;
import org.dcsa.tnt.service.EventSubscriptionService;
import org.dcsa.core.events.config.MessageServiceConfig;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventSubscriptionServiceImpl extends ExtendedBaseServiceImpl<EventSubscriptionRepository, EventSubscription, UUID> implements EventSubscriptionService {
    private final EventSubscriptionRepository eventSubscriptionRepository;
    private final MessageServiceConfig messageServiceConfig;
    private final TransportCallRepository transportCallRepository;


    @Override
    public EventSubscriptionRepository getRepository() {
        return eventSubscriptionRepository;
    }

    @Override
    protected Mono<EventSubscription> preCreateHook(EventSubscription eventSubscription) {
        byte[] secret = eventSubscription.getSecret();
        SignatureMethod signatureMethod;
        if (eventSubscription.getSignatureMethod() == null) {
            signatureMethod = messageServiceConfig.getDefaultSignatureMethod();
            eventSubscription.setSignatureMethod(signatureMethod);
        } else {
            signatureMethod = eventSubscription.getSignatureMethod();
        }
        if (secret == null || secret.length < 1) {
            return Mono.error(new CreateException("Please provide a non-empty \"secret\" attribute"));
        }
        if (signatureMethod.getMinKeyLength() == signatureMethod.getMaxKeyLength()) {
            if (secret.length != signatureMethod.getMinKeyLength()) {
                if (secret.length  > 1 && secret.length - 1 == signatureMethod.getMinKeyLength()
                        && Character.isSpaceChar(((int)secret[secret.length - 1]) & 0xff)) {
                    return Mono.error(new CreateException("The secret provided in the \"secret\" attribute must be exactly "
                            + signatureMethod.getMinKeyLength() + " bytes long (when deserialized).  Did you"
                            + " accidentally include a trailing newline/space in the secret?"));
                }
                return Mono.error(new CreateException("The secret provided in the \"secret\" attribute must be exactly "
                        + signatureMethod.getMinKeyLength() + " bytes long (when deserialized)"));
            }
        } else {
            if (secret.length < signatureMethod.getMinKeyLength()) {
                return Mono.error(new CreateException("The secret provided in the \"secret\" attribute must be at least "
                        + signatureMethod.getMinKeyLength() + " bytes long (when deserialized)"));
            }
            if (secret.length > signatureMethod.getMaxKeyLength()) {
                return Mono.error(new CreateException("The secret provided in the \"secret\" attribute must be at most "
                        + signatureMethod.getMaxKeyLength() + " bytes long (when deserialized)"));
            }
        }
        return checkEventSubscription(eventSubscription);
    }

    @Override
    protected Mono<EventSubscription> preUpdateHook(EventSubscription original, EventSubscription update) {
        return checkEventSubscription(update);
    }

    protected Mono<EventSubscription> checkEventSubscription(EventSubscription eventSubscription) {
        String vessel = eventSubscription.getVesselIMONumber();
        if (vessel != null){
            try{
                ValidationUtils.validateVesselIMONumber(vessel);
            } catch (Exception e){
                return Mono.error(new UpdateException(e.getLocalizedMessage()));
            }
        }
        // Ensure that the callback url at least looks valid.
        try {
            new URI(eventSubscription.getCallbackUrl());
        } catch (URISyntaxException e) {
            return Mono.error(new UpdateException("callbackUrl is invalid: " + e.getLocalizedMessage()));
        }
        return Mono.just(eventSubscription);
    }

    @Override
    public Flux<EventSubscription> findSubscriptionsFor(Event event) {
        switch (event.getEventType()){
            case EQUIPMENT:
                return findSubscriptionsFor((EquipmentEvent) event);
            case SHIPMENT:
                return findSubscriptionsFor((ShipmentEvent) event);
            case TRANSPORT:
                return findSubscriptionsFor((TransportEvent) event);
        }
        throw new IllegalArgumentException("Unsupported event type.");
    }

    private Flux<EventSubscription> findSubscriptionsFor(EquipmentEvent equipmentEvent) {
        // TODO: find values and call findByEquipmentEventFields
        return Flux.empty();
    }

    private Flux<EventSubscription> findSubscriptionsFor(ShipmentEvent shipmentEvent) {
        // TODO: find values and call findByShipmentEventFields
        return Flux.empty();
    }

    private Flux<EventSubscription> findSubscriptionsFor(TransportEvent transportEvent) {
        Mono<List<String>> carrierVoyageNumbers = transportCallRepository
                .findCarrierVoyageNumbersByTransportCallID(transportEvent.getTransportCallID())
                .collectList();
        Mono<List<String>> carrierServiceCodes = transportCallRepository
                .findCarrierServiceCodesByTransportCallID(transportEvent.getTransportCallID())
                .collectList();

        TransportEventTypeCode transportEventTypeCode = transportEvent.getTransportEventTypeCode();
        String vesselIMONumber = transportEvent.getTransportCall().getVessel().getVesselIMONumber();
        String transportCallID = transportEvent.getTransportCallID();

        List<DocumentReferenceTO> documentReferences = transportEvent.getDocumentReferences();
        List<String> carrierBookingReferences = documentReferences.stream()
                .filter(documentReferenceTO -> documentReferenceTO.getDocumentReferenceType() == DocumentReferenceType.BKG)
                .map(DocumentReferenceTO::getDocumentReferenceValue)
                .collect(Collectors.toList());
        List<String> transportDocumentReferences = documentReferences.stream()
                .filter(documentReferenceTO -> documentReferenceTO.getDocumentReferenceType() == DocumentReferenceType.TRD)
                .map(DocumentReferenceTO::getDocumentReferenceValue)
                .collect(Collectors.toList());

        Mono<List<String>> transportDocumentTypeCodes = Flux.fromIterable(transportDocumentReferences)
                .flatMap(transportCallRepository::findTransportDocumentTypeCodeByTransportDocumentReference)
                .collectList();

        return Mono.zip(carrierVoyageNumbers, carrierServiceCodes, transportDocumentTypeCodes).flatMapMany(vnScTc -> {
            List<String> voyageNumbers = vnScTc.getT1();
            List<String> serviceCodes = vnScTc.getT2();
            List<String> documentTypeCodes = vnScTc.getT3();

            return eventSubscriptionRepository.findByTransportEventFields(
                    voyageNumbers, serviceCodes,
                    transportEventTypeCode, vesselIMONumber, transportCallID,
                    carrierBookingReferences, transportDocumentReferences,
                    documentTypeCodes,
                    transportEvent.getEventType()
            );
        });
    }
}
