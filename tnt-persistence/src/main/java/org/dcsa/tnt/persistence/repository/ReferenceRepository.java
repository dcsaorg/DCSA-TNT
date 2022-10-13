package org.dcsa.tnt.persistence.repository;

import org.dcsa.tnt.persistence.entity.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, UUID>, JpaSpecificationExecutor<Reference> {
  List<Reference> findDocumentReferencesByTransportCallID(UUID transportCallID);
  List<Reference> findReferencesByUtilizedEquipmentID(UUID utilizedEquipmentID);
  List<Reference> findDocumentReferencesByLinkTypeAndDocumentID(
    String documentReferenceType, UUID documentID);
}

