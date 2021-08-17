package org.dcsa.tnt.controller;

import org.dcsa.core.events.controller.AbstractEventSubscriptionController;
import org.dcsa.core.events.service.EventSubscriptionTOService;
import org.dcsa.core.extendedrequest.ExtendedParameters;
import org.dcsa.tnt.model.transferobjects.TNTEventSubscriptionTO;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TNTEventSubscriptionTOController
    extends AbstractEventSubscriptionController<
        EventSubscriptionTOService<TNTEventSubscriptionTO>, TNTEventSubscriptionTO> {

  private final EventSubscriptionTOService<TNTEventSubscriptionTO> eventSubscriptionTOService;

  public TNTEventSubscriptionTOController(
      ExtendedParameters extendedParameters,
      R2dbcDialect r2dbcDialect,
      EventSubscriptionTOService<TNTEventSubscriptionTO> eventSubscriptionTOService) {
    super(extendedParameters, r2dbcDialect);
    this.eventSubscriptionTOService = eventSubscriptionTOService;
  }

  @Override
  public EventSubscriptionTOService<TNTEventSubscriptionTO> getService() {
    return eventSubscriptionTOService;
  }
}
