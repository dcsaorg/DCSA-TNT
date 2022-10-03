package org.dcsa.tnt.service.mapping;

import org.dcsa.tnt.persistence.entity.DocumentReference;
import org.dcsa.tnt.transferobjects.DocumentReferenceTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentReferenceMapper {
  @Mapping(target = "type", source = "documentReferenceType")
  @Mapping(target = "value", source = "documentReferenceValue")
  DocumentReferenceTO toTO(DocumentReference reference);

}
