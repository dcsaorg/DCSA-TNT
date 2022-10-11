package org.dcsa.tnt.service.mapping.domain;

import org.dcsa.tnt.persistence.entity.Reference;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DomainReferenceMapper {
  org.dcsa.tnt.service.domain.Reference toDomain(Reference reference);
}
