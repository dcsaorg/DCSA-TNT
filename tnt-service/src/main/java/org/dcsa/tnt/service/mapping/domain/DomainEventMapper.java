package org.dcsa.tnt.service.mapping.domain;

import org.dcsa.tnt.persistence.entity.EquipmentEvent;
import org.dcsa.tnt.persistence.entity.ShipmentEvent;
import org.dcsa.tnt.persistence.entity.TransportEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DomainEventMapper {
  org.dcsa.tnt.service.domain.EquipmentEvent toDomain(EquipmentEvent event);

  org.dcsa.tnt.service.domain.ShipmentEvent toDomain(ShipmentEvent event);

  org.dcsa.tnt.service.domain.TransportEvent toDomain(TransportEvent event);
}
