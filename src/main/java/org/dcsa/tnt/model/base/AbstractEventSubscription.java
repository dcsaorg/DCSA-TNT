package org.dcsa.tnt.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dcsa.core.model.AuditBase;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

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
}
