package com.example.portfolio.offering.read.dto;

import com.example.portfolio.offering.model.domain.Offering;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OfferingDTO {
  private UUID id;

  private String offeringName;
  private long activeSubscriberLimit;
  private List<SkuDTO> billingSKUs;

  public static OfferingDTO of(Offering offering, List<SkuDTO> billingSKUs) {
    return builder()
      .id(offering.getId())
      .offeringName(offering.getName())
      .billingSKUs(billingSKUs)
      .build();
  }
}
