package org.dcsa.tnt.service.mapping;

import org.dcsa.tnt.persistence.entity.Reference;
import org.dcsa.tnt.transferobjects.ReferenceTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReferenceMapper {
  ReferenceTO toTO(Reference reference);
}
