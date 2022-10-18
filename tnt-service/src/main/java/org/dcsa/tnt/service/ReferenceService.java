package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.entity.EquipmentEvent;
import org.dcsa.tnt.persistence.entity.ShipmentEvent;
import org.dcsa.tnt.persistence.entity.TransportEvent;
import org.dcsa.tnt.persistence.repository.ReferenceRepository;
import org.dcsa.tnt.service.domain.Reference;
import org.dcsa.tnt.service.mapping.domain.DomainReferenceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferenceService {
  private final DomainReferenceMapper referenceMapper;
  private final ReferenceRepository referenceRepository;

  public List<Reference> findFor(ShipmentEvent event) {
    return referenceRepository
      .findDocumentReferencesByLinkTypeAndDocumentID(
      event.getDocumentTypeCode().name(),event.getDocumentID())
      .stream()
      .map(referenceMapper::toDomain)
      .toList();
  }

  public List<Reference> findFor(EquipmentEvent event) {
    return referenceRepository
      .findReferencesByUtilizedEquipmentID(event.getUtilizedEquipmentID())
      .stream()
      .map(referenceMapper::toDomain)
      .toList();
  }

  public List<Reference> findFor(TransportEvent event) {
    return referenceRepository
      .findDocumentReferencesByTransportCallID(event.getTransportCall().getId())
      .stream()
      .map(referenceMapper::toDomain)
      .toList();
  }
}
