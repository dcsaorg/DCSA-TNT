package org.dcsa.tnt.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ShipmentEventTypeCode {
  RECE("Indicates that a document is received by the carrier or shipper"),
  DRFT("Indicates that a document is in draft mode being updated by either the shipper or the carrier."),
  PENA("Indicates that a document has been submitted by the carrier and is now awaiting approval by the shipper."),
  PENU("Indicates that the carrier requested an update from the shipper which is not received yet."),
  PENC("Indicates that a document has been submitted by the shipper and is now awaiting approval by the carrier."),
  REJE("Indicates that a document has been rejected by the carrier."),
  APPR("Indicates that a document has been approved by the counterpart."),
  ISSU("Indicates that a document has been issued by the carrier."),
  SURR("Indicates that a document has been surrendered by the customer to the carrier."),
  SUBM("Indicates that a document has been submitted by the customer to the carrier."),
  VOID("Cancellation of an original document."),
  CONF("Indicates that the document is confirmed."),
  REQS("A status indicator that can be used with a number of identifiers to denote that a certain activity, service or document has been requested by the carrier, customer or authorities. This status remains constant until the requested activity is  Completed."),
  CMPL("A status indicator that can be used with a number of activity identifiers to denote that a certain activity, service or document has been completed."),
  HOLD("A status indicator that can be used with a number of activity identifiers to denote that a container or shipment has been placed on hold i.e. can’t  progress in the process."),
  RELS("A status indicator that can be used with a number of activity identifiers to denote that a container or shipment has been released i.e. allowed to move from depot or terminal by authorities or service provider."),
  CANC("A status indicator to be used when the booking is cancelled by the Shipper"),
  ;

  @Getter
  private String description;
}
