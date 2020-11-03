package org.dcsa.tnt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dcsa.core.model.AuditBase;
import org.dcsa.core.model.GetId;
import org.dcsa.core.util.ValidationUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.Pattern;
import java.security.InvalidParameterException;
import java.util.UUID;

@Table("transport_call")
@Data
@NoArgsConstructor
public class TransportCall extends AuditBase implements GetId<UUID> {

    @Id
    @JsonProperty("transportCallID")
    private UUID id;

    @JsonProperty("scheduleID")
    @Column("schedule_id")
    private UUID scheduleId;

    @JsonProperty("carrierServiceCode")
    @Column("carrier_service_code")
    private String carrierServiceCode;

    @JsonProperty("vesselIMONumber")
    @Column("vessel_imo_number")
    @Pattern(regexp = "[0-9]{7}")
    private String vesselIMONumber;

    public void setVesselIMONumber(String vesselIMONumber) {
        ValidationUtils.validateVesselIMONumber(vesselIMONumber);
        this.vesselIMONumber = vesselIMONumber;
    }

    @JsonProperty("vesselName")
    @Column("vessel_name")
    private String vesselName;

    @JsonProperty("carrierVoyageNumber")
    @Column("carrier_voyage_number")
    private String carrierVoyageNumber;

    @JsonProperty("UNLocationCode")
    @Column("un_location_code")
    private String UNLocationCode;

    @JsonProperty("UNLocationName")
    @Column("un_location_name")
    private String UNLocationName;

    @JsonProperty("transportCallNumber")
    @Column("transport_call_number")
    private Integer transportCallNumber;

    @JsonProperty("facilityTypeCode")
    @Column("facility_type_code")
    private String facilityTypeCode;

    @JsonProperty("facilityCode")
    @Column("facility_code")
    private String facilityCode;

    @JsonProperty("otherFacility")
    @Column("other_facility")
    private String otherFacility;
}
