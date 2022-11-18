package org.dcsa.tnt.service.mapping.transferobject;

import org.dcsa.tnt.service.domain.Seal;
import org.dcsa.tnt.transferobjects.SealTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LocationTOMapper.class)
public interface SealMapper {
  @Mapping(source = "sealNumber", target = "number")
  @Mapping(source = "sealSourceCode", target = "source")
  @Mapping(source = "sealType", target = "type")
  SealTO toDTO(Seal seal);
}
