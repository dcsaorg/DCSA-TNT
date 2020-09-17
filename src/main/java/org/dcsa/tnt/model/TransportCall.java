package org.dcsa.tnt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dcsa.core.model.AuditBase;
import org.dcsa.core.model.GetId;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("transport_call")
@Data
@NoArgsConstructor
public class TransportCall extends AuditBase implements GetId<UUID> {
    @JsonProperty("transportCallID")
    private UUID id;

    @JsonProperty("scheduleID")
    @Column("schedule_id")
    private UUID schedule_id;

    @JsonProperty("carrierServiceCode")
    @Column("carrier_service_code")
    private String carrierServiceCode;

    @JsonProperty("vesselIMONumber")
    @Column("")
    private String vesselIMONumber;

    @JsonProperty("vesselName")
    @Column("")
    private String vesselName;

    @JsonProperty("carrierVoyageNumber")
    @Column("")
    private String carrierVoyageNumber;

    @JsonProperty("UNLocationCode")
    @Column("")
    private String UNLocationCode;

    @JsonProperty("UNLocationName")
    @Column("")
    private String UNLocationName;

    @JsonProperty("transportCallNumber")
    @Column("")
    private Integer transportCallNumber;

    @JsonProperty("facilityTypeCode")
    @Column("")
    private String facilityTypeCode;

    @JsonProperty("facilityCode")
    @Column("")
    private String facilityCode;

    @JsonProperty("otherFacility")
    @Column("")
    private String otherFacility;
}
