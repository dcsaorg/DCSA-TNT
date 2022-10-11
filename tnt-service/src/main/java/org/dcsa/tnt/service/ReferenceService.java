package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.entity.EquipmentEvent;
import org.dcsa.tnt.persistence.entity.ShipmentEvent;
import org.dcsa.tnt.persistence.entity.TransportEvent;
import org.dcsa.tnt.service.domain.Reference;
import org.dcsa.tnt.service.mapping.domain.DomainReferenceMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferenceService {
  private final DomainReferenceMapper domainReferenceMapper;

  public List<Reference> findFor(ShipmentEvent event) {
    // TODO DDT-1231
    return Collections.emptyList();
  }

  public List<Reference> findFor(EquipmentEvent event) {
    // TODO DDT-1231
    return Collections.emptyList();
  }

  public List<Reference> findFor(TransportEvent event) {
    // TODO DDT-1231
    return Collections.emptyList();
  }
}
