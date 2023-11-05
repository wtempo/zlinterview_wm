package com.example.portfolio.offering.read.dto;

import com.example.portfolio.offering.model.domain.Subscription;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SubscriptionDTO {
  private UUID id;
  private UUID userId;
  private UUID customerId;
  private boolean active;

  public static SubscriptionDTO of(Subscription subscription) {
    return SubscriptionDTO
      .builder()
      .id(subscription.getId())
      .userId(subscription.getUserId())
      .customerId(subscription.getCustomer().getId())
      .active(subscription.getActive())
      .build();
  }
}
