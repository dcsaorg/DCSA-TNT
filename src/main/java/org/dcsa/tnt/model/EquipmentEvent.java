package org.dcsa.tnt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dcsa.tnt.model.enums.EmptyIndicatorCode;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("equipment_event")
@Data
@NoArgsConstructor
@JsonTypeName("EQUIPMENT")
public class EquipmentEvent extends Event {

    @JsonProperty("equipmentReference")
    @Column("equipment_reference")
    private String equipmentReference;

    @JsonProperty("emptyIndicatorCode")
    @Column("empty_indicator_code")
    private EmptyIndicatorCode emptyIndicatorCode;

    @JsonProperty("transportCallID")
    @Column("transport_call_id")
    private UUID transportCallID;

}
