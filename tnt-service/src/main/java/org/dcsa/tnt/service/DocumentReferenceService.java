package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.entity.EquipmentEvent;
import org.dcsa.tnt.persistence.entity.ShipmentEvent;
import org.dcsa.tnt.persistence.entity.TransportEvent;
import org.dcsa.tnt.persistence.repository.DocumentReferenceRepository;
import org.dcsa.tnt.service.mapping.DocumentReferenceMapper;
import org.dcsa.tnt.transferobjects.DocumentReferenceTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentReferenceService {
  private final DocumentReferenceRepository documentReferenceRepository;
  private final DocumentReferenceMapper documentReferenceMapper;

  public List<DocumentReferenceTO> findFor(EquipmentEvent event) {
    return documentReferenceRepository
        .findDocumentReferencesByTransportCallID(event.getTransportCall().getId())
        .stream()
        .map(documentReferenceMapper::toTO)
        .toList();
  }

  public List<DocumentReferenceTO> findFor(TransportEvent event) {
    return documentReferenceRepository
        .findDocumentReferencesByTransportCallID(event.getTransportCall().getId())
        .stream()
        .map(documentReferenceMapper::toTO)
        .toList();
  }

  public List<DocumentReferenceTO> findFor(ShipmentEvent event) {
    return documentReferenceRepository
        .findDocumentReferencesByDocumentReferenceTypeAndDocumentID(
            event.getDocumentTypeCode().name(), event.getDocumentID())
        .stream()
        .map(documentReferenceMapper::toTO)
        .toList();
  }
}
