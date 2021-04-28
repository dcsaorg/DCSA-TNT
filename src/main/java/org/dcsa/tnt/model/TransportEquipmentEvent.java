package org.dcsa.tnt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dcsa.tnt.model.enums.EmptyIndicatorCode;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("transport_equipment_event")
@Data
@NoArgsConstructor
@JsonTypeName("TRANSPORTEQUIPMENT")
public class TransportEquipmentEvent extends Event {

    @JsonProperty("transportReference")
    @Column("transport_reference")
    private String transportReference;

    @JsonProperty("transportLegReference")
    @Column("transport_leg_reference")
    private String transportLegReference;

    @JsonProperty("equipmentReference")
    @Column("equipment_reference")
    private String equipmentReference;

    @JsonProperty("emptyIndicatorCode")
    @Column("empty_indicator_code")
    private EmptyIndicatorCode emptyIndicatorCode;

    @JsonProperty("facilityTypeCode")
    @Column("facility_type_code")
    private String facilityTypeCode;

    @JsonProperty("UNLocationCode")
    @Column("un_location_code")
    private String UNLocationCode;

    @JsonProperty("facilityCode")
    @Column("facility_code")
    private String facilityCode;

    @JsonProperty("otherFacility")
    @Column("other_facility")
    private String otherFacility;

    @JsonProperty("modeOfTransportCode")
    @Column("mode_of_transport_code")
    private String modeOfTransportCode;

}
