package com.example.portfolio.billing.read;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
public record CustomerBillingSummary(UUID customerId, BigDecimal seconds) {
  @JsonProperty("total")
  public BigDecimal getTotal() {
    var pricePerSecond = 100; // Would depend on offering SKUs etc. etc.
    return BigDecimal.valueOf(seconds.longValue() * pricePerSecond).setScale(2, RoundingMode.DOWN);
  }
}
