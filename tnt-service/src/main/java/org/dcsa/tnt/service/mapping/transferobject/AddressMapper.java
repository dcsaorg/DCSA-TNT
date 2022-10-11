package org.dcsa.tnt.service.mapping.transferobject;

import org.dcsa.tnt.service.domain.Address;
import org.dcsa.tnt.transferobjects.AddressTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
  AddressTO toDomain(Address address);
}
