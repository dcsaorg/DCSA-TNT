package org.dcsa.tnt.domain.persistence.repository;

import org.dcsa.tnt.domain.persistence.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository
  extends JpaRepository<Event, String>, JpaSpecificationExecutor<Event> { }
