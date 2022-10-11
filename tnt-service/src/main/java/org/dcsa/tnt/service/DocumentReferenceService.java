package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.entity.EquipmentEvent;
import org.dcsa.tnt.persistence.entity.ShipmentEvent;
import org.dcsa.tnt.persistence.entity.TransportEvent;
import org.dcsa.tnt.persistence.repository.DocumentReferenceRepository;
import org.dcsa.tnt.service.domain.DocumentReference;
import org.dcsa.tnt.service.mapping.domain.DomainDocumentReferenceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentReferenceService {
  private final DocumentReferenceRepository documentReferenceRepository;
  private final DomainDocumentReferenceMapper domainDocumentReferenceMapper;

  public List<DocumentReference> findFor(EquipmentEvent event) {
    return documentReferenceRepository
        .findDocumentReferencesByTransportCallID(event.getTransportCall().getId())
        .stream()
        .map(domainDocumentReferenceMapper::toDomain)
        .toList();
  }

  public List<DocumentReference> findFor(TransportEvent event) {
    return documentReferenceRepository
        .findDocumentReferencesByTransportCallID(event.getTransportCall().getId())
        .stream()
        .map(domainDocumentReferenceMapper::toDomain)
        .toList();
  }

  public List<DocumentReference> findFor(ShipmentEvent event) {
    return documentReferenceRepository
        .findDocumentReferencesByDocumentReferenceTypeAndDocumentID(
            event.getDocumentTypeCode().name(), event.getDocumentID())
        .stream()
        .map(domainDocumentReferenceMapper::toDomain)
        .toList();
  }
}
