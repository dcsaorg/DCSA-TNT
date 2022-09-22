package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.entity.AggregatedEquipmentEvent;
import org.dcsa.tnt.persistence.entity.AggregatedEvent;
import org.dcsa.tnt.persistence.entity.AggregatedShipmentEvent;
import org.dcsa.tnt.persistence.entity.AggregatedTransportEvent;
import org.dcsa.tnt.persistence.repository.AggregatedEventRepository;
import org.dcsa.tnt.service.mapping.AggregatedEventMapper;
import org.dcsa.tnt.transferobjects.EquipmentEventTO;
import org.dcsa.tnt.transferobjects.EventTO;
import org.dcsa.tnt.transferobjects.ShipmentEventTO;
import org.dcsa.tnt.transferobjects.TransportEventTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AggregatedEventService {
  private final AggregatedEventRepository aggregatedEventRepository;
  private final AggregatedEventMapper aggregatedEventMapper;

  private final ReferenceService referenceService;
  private final DocumentReferenceService documentReferenceService;
  private final SealService sealService;

  @Transactional
  public List<EventTO> findAll() {
    return aggregatedEventRepository.findAll()
      .stream()
      .map(this::toDTO)
      .toList();
  }

  @Transactional
  public EventTO findById(UUID id) {
    return aggregatedEventRepository.findById(id).map(this::toDTO).orElseThrow(() -> new RuntimeException("waaa"));
  }

  private EventTO toDTO(AggregatedEvent event) {
    if (event instanceof AggregatedEquipmentEvent) {
      return toDTO((AggregatedEquipmentEvent) event);
    } else if (event instanceof AggregatedShipmentEvent) {
      return toDTO((AggregatedShipmentEvent) event);
    } else if (event instanceof AggregatedTransportEvent) {
      return toDTO((AggregatedTransportEvent) event);
    }
    throw new IllegalStateException("Unknown type " + event.getClass().getName());
  }

  private EquipmentEventTO toDTO(AggregatedEquipmentEvent event) {
    EquipmentEventTO to = aggregatedEventMapper.toDTO(event);
    to.setDocumentReferences(documentReferenceService.findFor(event));
    to.setReferences(referenceService.findFor(event));
    to.setSeals(sealService.findFor(event));
    return to;
  }

  private ShipmentEventTO toDTO(AggregatedShipmentEvent event) {
    ShipmentEventTO to = aggregatedEventMapper.toDTO(event);
    to.setReferences(referenceService.findFor(event));
    return to;
  }

  private TransportEventTO toDTO(AggregatedTransportEvent event) {
    TransportEventTO to = aggregatedEventMapper.toDTO(event);
    to.setDocumentReferences(documentReferenceService.findFor(event));
    to.setReferences(referenceService.findFor(event));
    return to;
  }
}
