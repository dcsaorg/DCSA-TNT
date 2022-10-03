package org.dcsa.tnt.persistence.repository;

import org.dcsa.tnt.persistence.entity.Seal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SealRepository extends JpaRepository<Seal, UUID> {
  List<Seal> findByUtilizedEquipmentID(UUID utilizedEquipmentID);
}
