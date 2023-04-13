package org.dcsa.tnt.domain.valueobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dcsa.skernel.domain.persistence.entity.enums.FacilityTypeCode;
import org.dcsa.tnt.domain.valueobjects.enums.EmptyIndicatorCode;
import org.dcsa.tnt.domain.valueobjects.enums.EquipmentEventTypeCode;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class EquipmentEvent extends DomainEvent {
  private EquipmentEventTypeCode equipmentEventTypeCode;
  private String equipmentReference;
  private String ISOEquipmentCode;
  private UUID utilizedEquipmentID;
  private EmptyIndicatorCode emptyIndicatorCode;
  private TransportCall transportCall;
  private FacilityTypeCode facilityTypeCode;
  private boolean transshipmentMove;
  private Location eventLocation;

  private List<Seal> seals;
}
