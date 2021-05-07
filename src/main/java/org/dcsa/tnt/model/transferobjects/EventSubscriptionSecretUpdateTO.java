package org.dcsa.tnt.model.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EventSubscriptionSecretUpdateTO {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // Jackson encodes this in base64 by default
    protected byte[] secret;
}
