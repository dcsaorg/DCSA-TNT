package org.dcsa.tnt.transferobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EventSubscriptionWithSecretTO extends EventSubscriptionTO {
  @NotNull
  @Size(min = EventSubscriptionSecretTO.MIN_SECRET_SIZE, max = EventSubscriptionSecretTO.MAX_SECRET_SIZE)
  @ToString.Exclude
  private byte[] secret;
}
