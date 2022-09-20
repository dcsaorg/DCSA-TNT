package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.entity.AggregatedEquipmentEvent;
import org.dcsa.tnt.service.mapping.SealMapper;
import org.dcsa.tnt.transferobjects.SealTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SealService {
  private final SealMapper sealMapper;

  public List<SealTO> findFor(AggregatedEquipmentEvent event) {
    // TODO
    return Collections.emptyList();
  }
}
