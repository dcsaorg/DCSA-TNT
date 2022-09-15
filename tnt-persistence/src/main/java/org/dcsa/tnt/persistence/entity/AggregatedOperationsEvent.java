package org.dcsa.tnt.persistence.entity;

import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@ToString(callSuper = true)
@Entity
@DiscriminatorValue("OPERATIONS")
public class AggregatedOperationsEvent extends AggregatedEvent {
}
