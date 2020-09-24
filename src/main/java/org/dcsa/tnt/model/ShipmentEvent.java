package org.dcsa.tnt.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dcsa.tnt.model.enums.ShipmentInformationTypeCode;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("shipment_event")
@Data
@NoArgsConstructor
@JsonTypeName("SHIPMENT")
public class ShipmentEvent extends Event {

    @JsonIgnore
    @Column("shipment_id")
    private UUID shipmentId;

    @JsonProperty("shipmentInformationTypeCode")
    @Column("shipment_information_type_code")
    private ShipmentInformationTypeCode shipmentInformationTypeCode;

    public void setShipmentInformationTypeCode(ShipmentInformationTypeCode shipmentInformationTypeCode) {
        this.shipmentInformationTypeCode = shipmentInformationTypeCode;
    }

    public void setShipmentInformationTypeCode(String shipmentInformationTypeCode) {
        this.shipmentInformationTypeCode = ShipmentInformationTypeCode.valueOf(shipmentInformationTypeCode);
    }
}
