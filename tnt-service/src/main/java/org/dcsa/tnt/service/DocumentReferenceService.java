package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.entity.AggregatedEquipmentEvent;
import org.dcsa.tnt.persistence.entity.AggregatedTransportEvent;
import org.dcsa.tnt.transferobjects.DocumentReferenceTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentReferenceService {
  public List<DocumentReferenceTO> findFor(AggregatedEquipmentEvent event) {
    // TODO
    return Collections.emptyList();
  }

  public List<DocumentReferenceTO> findFor(AggregatedTransportEvent event) {
    // TODO
    return Collections.emptyList();
  }
}
