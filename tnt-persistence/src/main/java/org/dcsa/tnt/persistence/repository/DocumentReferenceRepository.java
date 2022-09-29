package org.dcsa.tnt.persistence.repository;

import org.dcsa.tnt.persistence.entity.DocumentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface DocumentReferenceRepository
    extends JpaRepository<DocumentReference, UUID>, JpaSpecificationExecutor<DocumentReference> {
  List<DocumentReference> findDocumentReferencesByTransportCallID(UUID transportCallID);

  List<DocumentReference> findDocumentReferencesByDocumentReferenceTypeAndDocumentID(
      String documentReferenceType, UUID documentID);
}
