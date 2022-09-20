package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.entity.AggregatedEquipmentEvent;
import org.dcsa.tnt.persistence.entity.AggregatedShipmentEvent;
import org.dcsa.tnt.persistence.entity.AggregatedTransportEvent;
import org.dcsa.tnt.service.mapping.ReferenceMapper;
import org.dcsa.tnt.transferobjects.ReferenceTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferenceService {
  private final ReferenceMapper referenceMapper;

  public List<ReferenceTO> findFor(AggregatedShipmentEvent event) {
    // TODO
    return Collections.emptyList();
  }

  public List<ReferenceTO> findFor(AggregatedEquipmentEvent event) {
    // TODO
    return Collections.emptyList();
  }

  public List<ReferenceTO> findFor(AggregatedTransportEvent event) {
    // TODO
    return Collections.emptyList();
  }
}
