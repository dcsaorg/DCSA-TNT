package org.dcsa.tnt.service.mapping;

import org.dcsa.tnt.domain.valueobjects.DomainEvent;
import org.dcsa.tnt.domain.valueobjects.EquipmentEvent;
import org.dcsa.tnt.domain.valueobjects.ShipmentEvent;
import org.dcsa.tnt.domain.valueobjects.TransportEvent;
import org.dcsa.tnt.transferobjects.EquipmentEventPayloadTO;
import org.dcsa.tnt.transferobjects.EventMetadataTO;
import org.dcsa.tnt.transferobjects.EventTO;
import org.dcsa.tnt.transferobjects.ShipmentEventPayloadTO;
import org.dcsa.tnt.transferobjects.TransportEventPayloadTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class EventMapper {
  public EventTO toDTO(DomainEvent event) {
    return EventTO.builder()
      .metadata(toMetadataTO(event))
      .payload(switch (event.getEventType()) {
        case EQUIPMENT -> toEquipmentEventPayloadTO((EquipmentEvent) event);
        case SHIPMENT -> toShipmentEventPayloadTO((ShipmentEvent) event);
        case TRANSPORT -> toTransportEventPayloadTO((TransportEvent) event);
      })
      .build();
  }

  protected abstract EventMetadataTO toMetadataTO(DomainEvent event);
  protected abstract EquipmentEventPayloadTO toEquipmentEventPayloadTO(EquipmentEvent event);
  protected abstract ShipmentEventPayloadTO toShipmentEventPayloadTO(ShipmentEvent event);
  protected abstract TransportEventPayloadTO toTransportEventPayloadTO(TransportEvent event);
}
