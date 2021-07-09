package org.dcsa.tnt.model.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dcsa.core.model.AuditBase;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class AbstractEventSubscription extends AuditBase {

    public String getBookingReference() {
        return carrierBookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.carrierBookingReference = bookingReference;
    }

    @Id
    @Column("subscription_id")
    private UUID subscriptionID;

    @Column("callback_url")
    @NotNull
    @NotEmpty
    private String callbackUrl;


    @Column("booking_reference")
    private String bookingReference;

    @Column("carrier_booking_reference")
    private String carrierBookingReference;


    @Column("equipment_reference")
    private String equipmentReference;


    @Column("shipment_event_type_code")
    private String shipmentEventTypeCode;

    @Column("carrier_service_code")
    private String carrierServiceCode;

    @Column("carrier_voyage_number")
    private String carrierVoyageNumber;

    @Column("vessel_imo_number")
    private String vesselIMONumber;

    @Column("transport_document_reference")
    private String transportDocumentReference;

    @Column("transport_event_type_code")
    private String transportEventTypeCode;

    @Column("transport_document_type_code")
    private String transportDocumentTypeCode;

    @Column("transport_call_id")
    private String transportCallID;

    @Column("secret")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // Jackson encodes this in base64 by default
    protected byte[] secret;
}
