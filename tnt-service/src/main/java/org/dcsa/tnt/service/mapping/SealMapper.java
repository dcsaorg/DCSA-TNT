package org.dcsa.tnt.service.mapping;

import org.dcsa.tnt.persistence.entity.Seal;
import org.dcsa.tnt.transferobjects.SealTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SealMapper {
  SealTO toTO(Seal seal);
}
