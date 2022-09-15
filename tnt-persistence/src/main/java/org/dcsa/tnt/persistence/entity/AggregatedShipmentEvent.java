package org.dcsa.tnt.persistence.entity;

import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@ToString(callSuper = true)
@Entity
@DiscriminatorValue("SHIPMENT")
public class AggregatedShipmentEvent extends AggregatedEvent {
}
