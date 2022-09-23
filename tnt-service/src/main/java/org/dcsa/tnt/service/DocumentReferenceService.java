package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.entity.EquipmentEvent;
import org.dcsa.tnt.persistence.entity.TransportEvent;
import org.dcsa.tnt.transferobjects.DocumentReferenceTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentReferenceService {
  public List<DocumentReferenceTO> findFor(EquipmentEvent event) {
    // TODO DDT-1232
    return Collections.emptyList();
  }

  public List<DocumentReferenceTO> findFor(TransportEvent event) {
    // TODO DDT-1232
    return Collections.emptyList();
  }
}
