package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.entity.EquipmentEvent;
import org.dcsa.tnt.persistence.repository.SealRepository;
import org.dcsa.tnt.service.domain.Seal;
import org.dcsa.tnt.service.mapping.domain.DomainSealMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SealService {
  private final DomainSealMapper domainSealMapper;
  private final SealRepository sealRepository;

  public List<Seal> findFor(EquipmentEvent event) {
    return sealRepository.findByUtilizedEquipmentID(event.getUtilizedEquipmentID()).stream()
        .map(domainSealMapper::toDomain)
        .toList();
  }
}
