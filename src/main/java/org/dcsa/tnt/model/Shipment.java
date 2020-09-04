package org.dcsa.tnt.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dcsa.core.model.AuditBase;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("shipment")
@Data
@NoArgsConstructor
public class Shipment extends AuditBase {

    @JsonProperty("shipmentID")
    @Column("shipment_id")
    private UUID id;

    @JsonProperty("bookingReference")
    @Column("booking_reference")
    private String bookingReference;

    @JsonProperty("bookingDateTime")
    @Column("booking_datetime")
    private OffsetDateTime bookingDateTime;

    @JsonProperty("billOfLading")
    @Column("blnumber")
    private String billOfLading;

    @JsonProperty("shipperName")
    @Column("shipper_name")
    private String shipperName;

    @JsonProperty("consigneeName")
    @Column("consignee_name")
    private String consigneeName;

    @JsonProperty("collectionOrigin")
    @Column("collection_origin")
    private String collectionOrigin;

    @JsonProperty("collectionDateTime")
    @Column("collection_datetime")
    private OffsetDateTime collectionDateTime;

    @JsonProperty("deliveryDestination")
    @Column("delivery_destination")
    private String deliveryDestination;

    @JsonProperty("deliveryDateTime")
    @Column("delivery_datetime")
    private OffsetDateTime deliveryDateTime;

    @JsonProperty("carrierCode")
    @Column("carrier_code")
    private String carrierCode;
}
