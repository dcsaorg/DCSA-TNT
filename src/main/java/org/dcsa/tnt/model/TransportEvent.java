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
    @Column("delay-reason-code")
    private String delayReasonCode;

    @JsonProperty("vesselScheduleChangeRemark")
    @Column("vessel-schedule-change-remark")
    private String vesselScheduleChangeRemark;

    @JsonProperty("transportCallId")
    @Column("transport_call_id")
    private UUID transportCallId;
}
