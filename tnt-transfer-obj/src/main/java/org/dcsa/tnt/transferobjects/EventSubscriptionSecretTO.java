package org.dcsa.tnt.transferobjects;

import lombok.Builder;

import javax.validation.constraints.NotBlank;

public record EventSubscriptionSecretTO(
  @NotBlank String secret
) {
  @Builder
  public EventSubscriptionSecretTO { }
}
