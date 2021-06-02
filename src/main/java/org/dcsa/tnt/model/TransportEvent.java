package org.dcsa.tnt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("transport_event")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("TRANSPORT")
public class TransportEvent extends Event {

    @Column("delay_reason_code")
    private String delayReasonCode;

    @Column("change_remark")
    private String changeRemark;

    @Column("transport_call_id")
    private String transportCallID;
}
