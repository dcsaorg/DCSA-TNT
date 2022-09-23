package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.errors.exceptions.NotFoundException;
import org.dcsa.tnt.persistence.entity.EquipmentEvent;
import org.dcsa.tnt.persistence.entity.ShipmentEvent;
import org.dcsa.tnt.persistence.entity.TransportEvent;
import org.dcsa.tnt.persistence.repository.EquipmentEventRepository;
import org.dcsa.tnt.persistence.repository.ShipmentEventRepository;
import org.dcsa.tnt.persistence.repository.TransportEventRepository;
import org.dcsa.tnt.service.mapping.EventMapper;
import org.dcsa.tnt.transferobjects.EquipmentEventTO;
import org.dcsa.tnt.transferobjects.EventTO;
import org.dcsa.tnt.transferobjects.ShipmentEventTO;
import org.dcsa.tnt.transferobjects.TransportEventTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
  private final EquipmentEventRepository equipmentEventRepository;
  private final TransportEventRepository transportEventRepository;
  private final ShipmentEventRepository shipmentEventRepository;

  private final EventMapper eventMapper;

  private final ReferenceService referenceService;
  private final DocumentReferenceService documentReferenceService;
  private final SealService sealService;

  @Transactional
  public List<EventTO> findAll() {
    return Collections.emptyList();
  }

  @Transactional
  public List<ShipmentEventTO> findAllShipmentEvents() {
    return shipmentEventRepository.findAll().stream()
      .map(this::toDTO)
      .toList();
  }

  @Transactional
  public List<EquipmentEventTO> findAllEquipmentEvents() {
    return equipmentEventRepository.findAll().stream()
      .map(this::toDTO)
      .toList();
  }

  @Transactional
  public List<TransportEventTO> findAllTransportEvents() {
    return transportEventRepository.findAll().stream()
      .map(this::toDTO)
      .toList();
  }

  public EquipmentEventTO findEquipmentEvent(UUID eventId) {
    return equipmentEventRepository.findById(eventId)
      .map(this::toDTO)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No EquipmentEvent with id = " + eventId));
  }

  public ShipmentEventTO findShipmentEvent(UUID eventId) {
    return shipmentEventRepository.findById(eventId)
      .map(this::toDTO)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No ShipmentEvent with id = " + eventId));
  }

  public TransportEventTO findTransportEvent(UUID eventId) {
    return transportEventRepository.findById(eventId)
      .map(this::toDTO)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No TransportEvent with id = " + eventId));
  }

  private EquipmentEventTO toDTO(EquipmentEvent event) {
    EquipmentEventTO to = eventMapper.toDTO(event);
    to.setDocumentReferences(documentReferenceService.findFor(event));
    to.setReferences(referenceService.findFor(event));
    to.setSeals(sealService.findFor(event));
    return to;
  }

  private ShipmentEventTO toDTO(ShipmentEvent event) {
    ShipmentEventTO to = eventMapper.toDTO(event);
    to.setReferences(referenceService.findFor(event));
    return to;
  }

  private TransportEventTO toDTO(TransportEvent event) {
    TransportEventTO to = eventMapper.toDTO(event);
    to.setDocumentReferences(documentReferenceService.findFor(event));
    to.setReferences(referenceService.findFor(event));
    return to;
  }
}
