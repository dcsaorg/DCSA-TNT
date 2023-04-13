package org.dcsa.tnt.domain.persistence.repository;

import org.dcsa.tnt.domain.persistence.entity.EventDocumentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventDocumentReferenceRepository extends JpaRepository<EventDocumentReference, UUID> { }
