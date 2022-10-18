package org.dcsa.tnt.service.mapping.transferobject;

import org.dcsa.tnt.service.domain.Reference;
import org.dcsa.tnt.transferobjects.ReferenceTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReferenceMapper {
  @Mapping(target = "type", source = "referenceType")
  @Mapping(target = "value", source = "referenceValue")
  ReferenceTO toTO(Reference eventReference);
}
