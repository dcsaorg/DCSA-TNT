package org.dcsa.tnt.service.mapping;

import org.dcsa.tnt.persistence.entity.UnmappedEvent;
import org.dcsa.tnt.transferobjects.UnmappedEventTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnmappedEventMapper {
  UnmappedEventTO toTO(UnmappedEvent unmappedEvent);
}
