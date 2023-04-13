package org.dcsa.tnt.service.unofficial;

import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.tnt.domain.valueobjects.DomainEvent;
import org.dcsa.tnt.domain.valueobjects.EquipmentEvent;
import org.dcsa.tnt.domain.valueobjects.RetractedEvent;
import org.dcsa.tnt.domain.valueobjects.ShipmentEvent;
import org.dcsa.tnt.domain.valueobjects.TransportEvent;
import org.dcsa.tnt.transferobjects.EquipmentEventPayloadTO;
import org.dcsa.tnt.transferobjects.EventMetadataTO;
import org.dcsa.tnt.transferobjects.EventTO;
import org.dcsa.tnt.transferobjects.ShipmentEventPayloadTO;
import org.dcsa.tnt.transferobjects.TransportEventPayloadTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UnofficialEventMapper {
  public DomainEvent toDomain(EventTO eventTO) {
    if (eventTO.metadata().retractedEventID() != null) {
      return toRetractedEvent(eventTO.metadata());
    } else if (eventTO.payload() instanceof EquipmentEventPayloadTO payloadTO) {
      return toEquipmentEvent(eventTO.metadata(), payloadTO);
    } else if (eventTO.payload() instanceof TransportEventPayloadTO payloadTO) {
      return toTransportEvent(eventTO.metadata(), payloadTO);
    } else if (eventTO.payload() instanceof ShipmentEventPayloadTO payloadTO) {
      return toShipmentEvent(eventTO.metadata(), payloadTO);
    } else {
      throw ConcreteRequestErrorMessageException.internalServerError("");
    }
  }

  protected abstract EquipmentEvent toEquipmentEvent(EventMetadataTO metadata, EquipmentEventPayloadTO payload);
  protected abstract TransportEvent toTransportEvent(EventMetadataTO metadata, TransportEventPayloadTO payload);
  protected abstract ShipmentEvent toShipmentEvent(EventMetadataTO metadata, ShipmentEventPayloadTO payload);
  protected abstract RetractedEvent toRetractedEvent(EventMetadataTO metadata);
}
