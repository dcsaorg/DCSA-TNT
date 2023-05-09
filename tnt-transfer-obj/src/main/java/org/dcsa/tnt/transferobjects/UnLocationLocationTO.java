package org.dcsa.tnt.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class UnLocationLocationTO extends LocationTO {
  @JsonProperty("UNLocationCode")
  @Size(max = 5)
  private String unLocationCode;
}
