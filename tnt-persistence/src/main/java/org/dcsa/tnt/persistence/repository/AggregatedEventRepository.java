package org.dcsa.tnt.persistence.repository;

import org.dcsa.tnt.persistence.entity.AggregatedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AggregatedEventRepository extends JpaRepository<AggregatedEvent, UUID> { }
