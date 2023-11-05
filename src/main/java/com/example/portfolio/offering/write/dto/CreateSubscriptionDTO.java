package com.example.portfolio.offering.write.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionDTO {
  @NotNull
  private UUID customerId;
  @NotNull
  private UUID userId;
  @NotNull
  private UUID offeringId;
}
