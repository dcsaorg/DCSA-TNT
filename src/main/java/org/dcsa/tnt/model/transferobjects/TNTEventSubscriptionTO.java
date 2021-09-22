package org.dcsa.tnt.model.transferobjects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dcsa.core.events.model.base.AbstractEventSubscription;
import org.dcsa.core.events.model.enums.*;
import org.dcsa.core.validator.EnumSubset;
import org.dcsa.core.validator.ValidVesselIMONumber;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TNTEventSubscriptionTO extends AbstractEventSubscription {

  @EnumSubset(anyOf = {"SHIPMENT", "TRANSPORT", "EQUIPMENT"})
  private List<EventType> eventType;

  private List<ShipmentEventTypeCode> shipmentEventTypeCode;

  private String carrierBookingReference;

  private String transportDocumentReference;

  private List<TransportDocumentTypeCode> transportDocumentTypeCode;

  private List<TransportEventTypeCode> transportEventTypeCode;

  private String transportCallID;

  @ValidVesselIMONumber(allowNull = true)
  private String vesselIMONumber;

  private String carrierVoyageNumber;

  private String carrierServiceCode;

  private List<EquipmentEventTypeCode> equipmentEventTypeCode;

  private String equipmentReference;
}
