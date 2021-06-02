package org.dcsa.tnt.model.transferobjects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dcsa.core.events.model.enums.EventType;
import org.dcsa.tnt.model.base.AbstractEventSubscription;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventSubscriptionTO extends AbstractEventSubscription {

    // API Spec uses singular even though it is a list
    private List<EventType> eventType;
}
