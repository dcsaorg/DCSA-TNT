package org.dcsa.tnt.service.mapping;

import org.dcsa.skernel.infrastructure.transferobject.AddressTO;
import org.dcsa.tnt.domain.valueobjects.*;
import org.dcsa.tnt.transferobjects.*;
import org.dcsa.tnt.transferobjects.enums.LocationType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class EventMapper {
  public EventTO toDTO(DomainEvent event) {
    return EventTO.builder()
        .metadata(toMetadataTO(event))
        .payload(
            switch (event.getEventType()) {
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

  protected LocationTO locationToLocationTO(Location location) {

    LocationTO loc = null;

    if (null != location) {

      if (null != location.facilityCode()) {
        loc =
            FacilityLocationTO.builder()
                .locationName(location.locationName())
                .locationType(LocationType.FACI)
                .unLocationCode(location.UNLocationCode())
                .facilityCode(location.facilityCode())
                .facilityCodeListProvider(location.facilityCodeListProvider())
                .build();

      } else if (null != location.address()) {

        Address address = location.address();

        return AddressLocationTO.builder()
            .locationName(location.locationName())
            .locationType(LocationType.ADDR)
            .address(
                AddressTO.builder()
                    .name(address.name())
                    .street(address.street())
                    .streetNumber(address.streetNumber())
                    .floor(address.floor())
                    .postCode(address.postCode())
                    .city(address.city())
                    .stateRegion(address.stateRegion())
                    .country(address.country())
                    .build())
            .build();

      } else if (null != location.latitude() && null != location.longitude()) {

        loc =
            GeoLocationTO.builder()
                .locationName(location.locationName())
                .locationType(LocationType.GEOL)
                .latitude(location.latitude())
                .longitude(location.longitude())
                .build();

      } else {

        loc =
            UnLocationLocationTO.builder()
                .locationName(location.locationName())
                .locationType(LocationType.UNLO)
                .unLocationCode(location.UNLocationCode())
                .build();
      }
    }

    return loc;
  }
}
