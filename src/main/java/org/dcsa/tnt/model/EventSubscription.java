package org.dcsa.tnt.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dcsa.tnt.model.base.AbstractEventSubscription;
import org.springframework.data.relational.core.mapping.Table;

@Table("event_subscription")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventSubscription extends AbstractEventSubscription {

}
