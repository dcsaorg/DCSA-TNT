package org.dcsa.tnt.service.mapping;

import org.dcsa.tnt.persistence.entity.AggregatedEquipmentEvent;
import org.dcsa.tnt.persistence.entity.AggregatedEvent;
import org.dcsa.tnt.persistence.entity.AggregatedShipmentEvent;
import org.dcsa.tnt.persistence.entity.AggregatedTransportEvent;
import org.dcsa.tnt.transferobjects.EquipmentEventTO;
import org.dcsa.tnt.transferobjects.EventTO;
import org.dcsa.tnt.transferobjects.ShipmentEventTO;
import org.dcsa.tnt.transferobjects.TransportEventTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AggregatedEventMapper {
  EquipmentEventTO toDTO(AggregatedEquipmentEvent event);

  ShipmentEventTO toDTO(AggregatedShipmentEvent event);

  TransportEventTO toDTO(AggregatedTransportEvent event);
}
