package org.dcsa.tnt.service.mapping.domain;

import org.dcsa.tnt.persistence.entity.DocumentReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DomainDocumentReferenceMapper {
  @Mapping(target = "type", source = "documentReferenceType")
  @Mapping(target = "value", source = "documentReferenceValue")
  org.dcsa.tnt.service.domain.DocumentReference toDomain(DocumentReference reference);

}
