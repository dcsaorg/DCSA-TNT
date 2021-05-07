package org.dcsa.tnt.model.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dcsa.core.model.AuditBase;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class AbstractEventSubscription extends AuditBase {

    @Id
    @Column("subscription_id")
    private UUID subscriptionID;

    @Column("callback_url")
    private String callbackUrl;

    @Column("booking_reference")
    private String bookingReference;

    @Column("equipment_reference")
    private String equipmentReference;

    @Column("secret")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // Jackson encodes this in base64 by default
    protected byte[] secret;
}
