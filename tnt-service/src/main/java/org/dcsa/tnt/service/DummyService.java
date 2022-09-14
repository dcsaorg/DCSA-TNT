package org.dcsa.tnt.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.persistence.repository.UnmappedEventRepository;
import org.dcsa.tnt.service.mapping.UnmappedEventMapper;
import org.dcsa.tnt.transferobjects.UnmappedEventTO;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO: Remove when there is actual implementation
@Service
@RequiredArgsConstructor
public class DummyService {
  private final UnmappedEventRepository unmappedEventRepository;
  private final UnmappedEventMapper unmappedEventMapper;

  public List<UnmappedEventTO> findAll() {
    return unmappedEventRepository.findAll()
      .stream().map(unmappedEventMapper::toTO).toList();
  }
}
