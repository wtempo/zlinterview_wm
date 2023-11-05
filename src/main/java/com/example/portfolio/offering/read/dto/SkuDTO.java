package com.example.portfolio.offering.read.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuDTO {
  private UUID id;
  private String code;
  private BigDecimal costPerTimeUnit;
}
