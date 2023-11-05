package com.example.portfolio.offering.read.dto;

import com.example.portfolio.offering.model.domain.CustomerOffering;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AvailableOfferingDTO {
  private UUID offeringId;
  private String name;
  private long activeSubscriberCount;
  private long activeSubscriberLimit;
  private List<SkuDTO> billingSKUs;

  public static AvailableOfferingDTO of(
    CustomerOffering customerOffering,
    long activeSubscriberCount,
    List<SkuDTO> billingSKUs
  ) {
    return AvailableOfferingDTO
      .builder()
      .offeringId(customerOffering.getOffering().getId())
      .name(customerOffering.getOffering().getName())
      .activeSubscriberCount(activeSubscriberCount)
      .activeSubscriberLimit(customerOffering.getActiveSubscriptionLimit())
      .billingSKUs(billingSKUs)
      .build();
  }
}
