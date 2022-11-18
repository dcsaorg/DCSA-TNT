package org.dcsa.tnt.service.mapping.transferobject;

import org.dcsa.skernel.infrastructure.transferobject.AddressTO;
import org.dcsa.tnt.service.domain.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressTOMapper {
  AddressTO toDomain(Address address);
}
