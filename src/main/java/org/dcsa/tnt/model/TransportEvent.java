package org.dcsa.tnt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("transport_event")
@Data
@NoArgsConstructor
@JsonTypeName("TRANSPORT")
public class TransportEvent extends Event {

    @JsonProperty("delayReasonCode")
    @Column("delay_reason_code")
    private String delayReasonCode;

    @JsonProperty("vesselScheduleChangeRemark")
    @Column("vessel_schedule_change_remark")
    private String vesselScheduleChangeRemark;

    @JsonProperty("transportCallID")
    @Column("transport_call_id")
    private UUID transportCallID;
}
