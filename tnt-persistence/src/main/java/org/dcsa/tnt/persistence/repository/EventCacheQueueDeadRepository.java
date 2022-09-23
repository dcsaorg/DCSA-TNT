package org.dcsa.tnt.persistence.repository;

import org.dcsa.tnt.persistence.entity.EventCacheQueueDead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventCacheQueueDeadRepository extends JpaRepository<EventCacheQueueDead, UUID> { }
