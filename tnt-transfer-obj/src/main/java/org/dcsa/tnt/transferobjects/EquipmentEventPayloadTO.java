package org.dcsa.tnt.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dcsa.tnt.transferobjects.LocationTO;
import org.dcsa.tnt.transferobjects.EventPayloadTO.EventPayloadTOWithTransportCall;
import org.dcsa.tnt.transferobjects.enums.EmptyIndicatorCode;
import org.dcsa.tnt.transferobjects.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.transferobjects.enums.FacilityTypeCode;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class EquipmentEventPayloadTO extends EventPayloadTO implements EventPayloadTOWithTransportCall {
  private EquipmentEventTypeCode equipmentEventTypeCode;
  private String equipmentReference;
  private String ISOEquipmentCode;
  private EmptyIndicatorCode emptyIndicatorCode;
  @JsonProperty("isTransshipmentMove") private boolean transshipmentMove;
  private LocationTO eventLocation;
  private TransportCallTO transportCall;
  private FacilityTypeCode facilityTypeCode;

  private List<SealTO> seals;
}
