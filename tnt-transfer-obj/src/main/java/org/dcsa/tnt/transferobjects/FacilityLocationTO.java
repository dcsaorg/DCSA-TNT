package org.dcsa.tnt.transferobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dcsa.tnt.transferobjects.enums.FacilityCodeListProvider;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FacilityLocationTO extends LocationTO {
  private String UNLocationCode;
  private String facilityCode;
  private FacilityCodeListProvider facilityCodeListProvider;
}
