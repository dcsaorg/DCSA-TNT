package org.dcsa.tnt.transferobjects;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.dcsa.skernel.infrastructure.transferobject.AddressTO;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class AddressLocationTO extends LocationTO {
  private AddressTO address;
}
