package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.entity.EquipmentEvent;
import org.dcsa.tnt.persistence.repository.SealRepository;
import org.dcsa.tnt.service.mapping.SealMapper;
import org.dcsa.tnt.transferobjects.SealTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SealService {
  private final SealMapper sealMapper;
  private final SealRepository sealRepository;

  public List<SealTO> findFor(EquipmentEvent event) {
    return sealRepository.findByUtilizedEquipmentID(event.getUtilizedEquipmentID()).stream()
        .map(sealMapper::toTO)
        .toList();
  }
}
