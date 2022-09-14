package org.dcsa.tnt.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.tnt.service.DummyService;
import org.dcsa.tnt.transferobjects.UnmappedEventTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// TODO: Remove when there is actual implementation
@RestController
@RequiredArgsConstructor
public class DummyController {
  private final DummyService dummyService;

  @GetMapping(path = "/unmapped-events")
  @ResponseStatus(HttpStatus.OK)
  public List<UnmappedEventTO> findAll() {
    return dummyService.findAll();
  }
}
