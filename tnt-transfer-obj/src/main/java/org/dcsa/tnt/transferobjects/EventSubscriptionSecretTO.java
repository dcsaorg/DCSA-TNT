package org.dcsa.tnt.transferobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSubscriptionSecretTO {
  public static final int MIN_SECRET_SIZE = 8;
  public static final int MAX_SECRET_SIZE = 1024;

  @NotNull
  @Size(min = MIN_SECRET_SIZE, max = MAX_SECRET_SIZE)
  @ToString.Exclude
  private byte[] secret;
}
