package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.repository.AggregatedEventRepository;
import org.dcsa.tnt.service.mapping.AggregatedEventMapper;
import org.dcsa.tnt.transferobjects.AggregatedEventTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AggregatedEventService {
  private final AggregatedEventRepository aggregatedEventRepository;
  private final AggregatedEventMapper aggregatedEventMapper;

  public List<AggregatedEventTO> findAll() {
    return aggregatedEventRepository.findAll().stream().map(aggregatedEventMapper::toDTO).toList();
  }
}
