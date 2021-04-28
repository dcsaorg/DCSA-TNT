package org.dcsa.tnt.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("shipment_event")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("SHIPMENT")
public class ShipmentEvent extends Event {

    @JsonProperty("shipmentInformationTypeCode")
    @Column("shipment_information_type_code")
    private String shipmentInformationTypeCode;

    @JsonProperty("shipmentID")
    @Column("shipment_id")
    private UUID shipmentId;
}
