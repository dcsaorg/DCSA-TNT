package org.dcsa.tnt.service.mapping;

import org.dcsa.tnt.persistence.entity.EquipmentEvent;
import org.dcsa.tnt.persistence.entity.ShipmentEvent;
import org.dcsa.tnt.persistence.entity.TransportEvent;
import org.dcsa.tnt.transferobjects.EquipmentEventTO;
import org.dcsa.tnt.transferobjects.ShipmentEventTO;
import org.dcsa.tnt.transferobjects.TransportEventTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
  EquipmentEventTO toDTO(EquipmentEvent event);

  ShipmentEventTO toDTO(ShipmentEvent event);

  TransportEventTO toDTO(TransportEvent event);
}
