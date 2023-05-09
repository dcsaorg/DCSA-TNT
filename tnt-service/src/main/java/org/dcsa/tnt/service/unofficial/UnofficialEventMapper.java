package org.dcsa.tnt.service.unofficial;

import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.transferobject.AddressTO;
import org.dcsa.tnt.domain.valueobjects.*;
import org.dcsa.tnt.transferobjects.*;
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

  protected abstract EquipmentEvent toEquipmentEvent(
      EventMetadataTO metadata, EquipmentEventPayloadTO payload);

  protected abstract TransportEvent toTransportEvent(
      EventMetadataTO metadata, TransportEventPayloadTO payload);

  protected abstract ShipmentEvent toShipmentEvent(
      EventMetadataTO metadata, ShipmentEventPayloadTO payload);

  protected abstract RetractedEvent toRetractedEvent(EventMetadataTO metadata);

  protected Location locationTOToLocation(LocationTO locationTO) {

    if (locationTO == null) {
      return null;
    }

    Location.LocationBuilder location = Location.builder();

    location.locationName(locationTO.getLocationName());

    if (locationTO instanceof UnLocationLocationTO unLocationLocationTO) {

      location.UNLocationCode(unLocationLocationTO.getUnLocationCode());

    } else if (locationTO instanceof FacilityLocationTO facilityLocationTO) {

      location.UNLocationCode(facilityLocationTO.getUnLocationCode())
          .facilityCode(facilityLocationTO.getFacilityCode())
          .facilityCodeListProvider(facilityLocationTO.getFacilityCodeListProvider());
    } else if (locationTO instanceof AddressLocationTO addressLocationTO) {

      AddressTO address = addressLocationTO.getAddress();
      location.address(
          Address.builder()
              .name(address.name())
              .street(address.street())
              .streetNumber(address.streetNumber())
              .floor(address.floor())
              .postCode(address.postCode())
              .city(address.city())
              .stateRegion(address.stateRegion())
              .country(address.country())
              .build());

    } else if (locationTO instanceof GeoLocationTO geoLocationTO) {

      location.latitude(geoLocationTO.getLatitude()).longitude(geoLocationTO.getLongitude());
    } else {

      throw new RuntimeException("Invalid location instance.");
    }

    return location.build();
  }
}
