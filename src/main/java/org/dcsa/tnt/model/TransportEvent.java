package org.dcsa.tnt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("transport_event")
@Data
@NoArgsConstructor
@JsonTypeName("TRANSPORT")
public class TransportEvent extends Event {

    @JsonProperty("transportReference")
    @Column("transport_reference")
    private String transportReference;

    @JsonProperty("transportLegReference")
    @Column("transport_leg_reference")
    private String transportLegReference;

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
