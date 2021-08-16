package org.dcsa.tnt.model.transferobjects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dcsa.core.events.model.base.AbstractEventSubscription;
import org.dcsa.core.validator.EnumSubset;

@Data
@EqualsAndHashCode(callSuper = true)
public class TNTEventSubscriptionTO extends AbstractEventSubscription {

  // API Spec uses singular even though it is a list
  @EnumSubset(anyOf = {"SHIPMENT", "TRANSPORT", "EQUIPMENT"})
  private String eventType;

  private String shipmentEventTypeCode;

  private String carrierBookingReference;

  private String transportDocumentReference;

  private String transportDocumentTypeCode;

  private String transportEventTypeCode;

  private String transportCallID;

  private String vesselIMONumber;

  private String carrierVoyageNumber;

  private String carrierServiceCode;

  private String equipmentEventTypeCode;

  private String equipmentReference;
}
