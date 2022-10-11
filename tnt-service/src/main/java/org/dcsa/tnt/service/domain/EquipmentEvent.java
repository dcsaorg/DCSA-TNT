package org.dcsa.tnt.service.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.dcsa.skernel.domain.persistence.entity.enums.FacilityTypeCode;
import org.dcsa.tnt.persistence.entity.enums.EmptyIndicatorCode;
import org.dcsa.tnt.persistence.entity.enums.EquipmentEventTypeCode;
import org.dcsa.tnt.persistence.entity.enums.EventType;

import java.util.List;
import java.util.UUID;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EquipmentEvent extends Event {
  private EquipmentEventTypeCode equipmentEventTypeCode;
  private Equipment equipment;
  private UUID utilizedEquipmentID;
  private EmptyIndicatorCode emptyIndicatorCode;
  private TransportCall transportCall;
  private FacilityTypeCode facilityTypeCode;
  private boolean transshipmentMove;
  private Location eventLocation;

  private List<Seal> seals;

  public EquipmentEvent() {
    setEventType(EventType.EQUIPMENT);
  }
}
