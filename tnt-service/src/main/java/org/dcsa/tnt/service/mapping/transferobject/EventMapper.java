package org.dcsa.tnt.service.mapping.transferobject;

import org.dcsa.tnt.service.domain.EquipmentEvent;
import org.dcsa.tnt.service.domain.Event;
import org.dcsa.tnt.service.domain.ShipmentEvent;
import org.dcsa.tnt.service.domain.TransportEvent;
import org.dcsa.tnt.transferobjects.EquipmentEventPayloadTO;
import org.dcsa.tnt.transferobjects.EventMetadataTO;
import org.dcsa.tnt.transferobjects.EventTO;
import org.dcsa.tnt.transferobjects.ShipmentEventPayloadTO;
import org.dcsa.tnt.transferobjects.TransportEventPayloadTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
  componentModel = "spring",
  uses = {
    TransportCallMapper.class,
    LocationMapper.class,
    SealMapper.class
  }
)
public interface EventMapper {
  default EventTO toDTO(Event event) {
    return EventTO.builder()
      .metadata(toMetadataTO(event))
      .payload(switch (event.getEventType()) {
        case EQUIPMENT -> toEquipmentEventPayloadTO((EquipmentEvent) event);
        case SHIPMENT -> toShipmentEventPayloadTO((ShipmentEvent) event);
        case TRANSPORT -> toTransportEventPayloadTO((TransportEvent) event);
      })
      .build();
  }

  EventMetadataTO toMetadataTO(Event event);

  @Mapping(source = "equipment.equipmentReference", target = "equipmentReference")
  @Mapping(source = "equipment.ISOEquipmentCode", target = "ISOEquipmentCode")
  EquipmentEventPayloadTO toEquipmentEventPayloadTO(EquipmentEvent event);

  ShipmentEventPayloadTO toShipmentEventPayloadTO(ShipmentEvent event);

  TransportEventPayloadTO toTransportEventPayloadTO(TransportEvent event);
}
