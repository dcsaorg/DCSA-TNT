package org.dcsa.tnt.service.mapping;

import org.dcsa.tnt.persistence.entity.EventSubscription;
import org.dcsa.tnt.persistence.entity.EventSubscriptionDocumentTypeCode;
import org.dcsa.tnt.persistence.entity.EventSubscriptionEquipmentEventTypeCode;
import org.dcsa.tnt.persistence.entity.EventSubscriptionEventType;
import org.dcsa.tnt.persistence.entity.EventSubscriptionShipmentEventTypeCode;
import org.dcsa.tnt.persistence.entity.EventSubscriptionTransportEventTypeCode;
import org.dcsa.tnt.transferobjects.EventSubscriptionWithIdTO;
import org.dcsa.tnt.transferobjects.EventSubscriptionWithSecretTO;
import org.dcsa.tnt.transferobjects.enums.DocumentTypeCode;
import org.dcsa.tnt.transferobjects.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.transferobjects.enums.EventType;
import org.dcsa.tnt.transferobjects.enums.ShipmentEventTypeCode;
import org.dcsa.tnt.transferobjects.enums.TransportEventTypeCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventSubscriptionMapper {
  EventSubscriptionWithIdTO toDTO(EventSubscription eventSubscription);

  @Mapping(source = "transportEventTypeCodes", target = "transportEventTypeCodes", ignore = true)
  @Mapping(source = "eventTypes", target = "eventTypes", ignore = true)
  @Mapping(source = "shipmentEventTypeCodes", target = "shipmentEventTypeCodes", ignore = true)
  @Mapping(source = "documentTypeCodes", target = "documentTypeCodes", ignore = true)
  @Mapping(source = "equipmentEventTypeCodes", target = "equipmentEventTypeCodes", ignore = true)
  EventSubscription toDAO(EventSubscriptionWithSecretTO eventSubscription);

  EventType toDTO(org.dcsa.tnt.persistence.entity.enums.EventType src);
  default EventType toDTO(EventSubscriptionEventType src) {
    return toDTO(src.getValue());
  }

  TransportEventTypeCode toDTO(org.dcsa.tnt.persistence.entity.enums.TransportEventTypeCode src);
  default TransportEventTypeCode toDTO(EventSubscriptionTransportEventTypeCode src) {
    return toDTO(src.getValue());
  }

  ShipmentEventTypeCode toDTO(org.dcsa.tnt.persistence.entity.enums.ShipmentEventTypeCode src);
  default ShipmentEventTypeCode toDTO(EventSubscriptionShipmentEventTypeCode src) {
    return toDTO(src.getValue());
  }

  EquipmentEventTypeCode toDTO(org.dcsa.tnt.persistence.entity.enums.EquipmentEventTypeCode src);
  default EquipmentEventTypeCode toDTO(EventSubscriptionEquipmentEventTypeCode src) {
    return toDTO(src.getValue());
  }

  DocumentTypeCode toDTO(org.dcsa.tnt.persistence.entity.enums.DocumentTypeCode src);
  default DocumentTypeCode toDTO(EventSubscriptionDocumentTypeCode src) {
    return toDTO(src.getValue());
  }

  org.dcsa.tnt.persistence.entity.enums.EventType toDAO(EventType src);

  org.dcsa.tnt.persistence.entity.enums.TransportEventTypeCode toDAO(TransportEventTypeCode src);

  org.dcsa.tnt.persistence.entity.enums.ShipmentEventTypeCode toDAO(ShipmentEventTypeCode src);

  org.dcsa.tnt.persistence.entity.enums.EquipmentEventTypeCode toDAO(EquipmentEventTypeCode src);

  org.dcsa.tnt.persistence.entity.enums.DocumentTypeCode toDAO(DocumentTypeCode src);
}
