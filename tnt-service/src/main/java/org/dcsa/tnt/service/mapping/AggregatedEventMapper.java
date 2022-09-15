package org.dcsa.tnt.service.mapping;

import org.dcsa.tnt.persistence.entity.AggregatedEquipmentEvent;
import org.dcsa.tnt.persistence.entity.AggregatedEvent;
import org.dcsa.tnt.persistence.entity.AggregatedOperationsEvent;
import org.dcsa.tnt.persistence.entity.AggregatedShipmentEvent;
import org.dcsa.tnt.persistence.entity.AggregatedTransportEvent;
import org.dcsa.tnt.transferobjects.AggregatedEquipmentEventTO;
import org.dcsa.tnt.transferobjects.AggregatedEventTO;
import org.dcsa.tnt.transferobjects.AggregatedOperationsEventTO;
import org.dcsa.tnt.transferobjects.AggregatedShipmentEventTO;
import org.dcsa.tnt.transferobjects.AggregatedTransportEventTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AggregatedEventMapper {
  default AggregatedEventTO toDTO(AggregatedEvent event) {
    if (event == null) {
      return null;
    } else if (event instanceof AggregatedEquipmentEvent) {
      return toDTO((AggregatedEquipmentEvent) event);
    } else if (event instanceof AggregatedShipmentEvent) {
      return toDTO((AggregatedShipmentEvent) event);
    } else if (event instanceof AggregatedTransportEvent) {
      return toDTO((AggregatedTransportEvent) event);
    } else if (event instanceof AggregatedOperationsEvent) {
      return toDTO((AggregatedOperationsEvent) event);
    }
    throw new IllegalArgumentException("Unknown type " + event.getClass().getName());
  }

  AggregatedEquipmentEventTO toDTO(AggregatedEquipmentEvent event);

  AggregatedShipmentEventTO toDTO(AggregatedShipmentEvent event);

  AggregatedTransportEventTO toDTO(AggregatedTransportEvent event);

  AggregatedOperationsEventTO toDTO(AggregatedOperationsEvent event);
}
