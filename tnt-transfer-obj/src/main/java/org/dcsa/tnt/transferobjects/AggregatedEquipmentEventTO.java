package org.dcsa.tnt.transferobjects;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class AggregatedEquipmentEventTO extends AggregatedEventTO {
  private String equipmentReference;
}
