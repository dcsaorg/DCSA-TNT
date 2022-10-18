package org.dcsa.tnt.persistence.repository;

import org.dcsa.tnt.persistence.entity.EventReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReferenceRepository extends JpaRepository<EventReference, UUID>, JpaSpecificationExecutor<EventReference> {
  List<EventReference> findDocumentReferencesByTransportCallID(UUID transportCallID);
  List<EventReference> findReferencesByUtilizedEquipmentID(UUID utilizedEquipmentID);
  List<EventReference> findDocumentReferencesByLinkTypeAndDocumentID(
    String documentReferenceType, UUID documentID);
}

