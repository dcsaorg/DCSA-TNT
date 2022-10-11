package org.dcsa.tnt.service.mapping.domain;

import org.dcsa.tnt.persistence.entity.Seal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DomainSealMapper {
  org.dcsa.tnt.service.domain.Seal toDomain(Seal seal);
}
