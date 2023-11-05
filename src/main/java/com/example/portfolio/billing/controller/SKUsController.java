package com.example.portfolio.billing.controller;


import com.example.portfolio.offering.read.dto.SkuDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/skus")
public class SKUsController {

  // For illustration purpose only. Returns dummy data.
  @GetMapping()
  public ResponseEntity<List<SkuDTO>> getSKUs(@RequestParam("sku_ids") List<UUID> skuIds) {
    return ResponseEntity.ok(
      skuIds.stream().map(id ->
        SkuDTO
          .builder()
          .id(id)
          .code("CODE")
          .costPerTimeUnit(BigDecimal.valueOf(
            new Random().nextDouble()
          ).setScale(4, RoundingMode.HALF_UP))
          .build()
      ).collect(Collectors.toList())
    );
  }
}
