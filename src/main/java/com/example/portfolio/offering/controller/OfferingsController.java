package com.example.portfolio.offering.controller;

import com.example.portfolio.offering.read.OfferingDAO;
import com.example.portfolio.offering.read.dto.OfferingDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/offerings")
public class OfferingsController {
  private final OfferingDAO offeringDAO;

  @GetMapping(value = "/{offering_id}")
  public ResponseEntity<OfferingDTO> getOffering(@PathVariable("offering_id") UUID offeringId) {
    return ResponseEntity.ok(
      offeringDAO.getSingle(offeringId)
    );
  }

  @GetMapping(value = "/")
  public ResponseEntity<List<OfferingDTO>> getOfferings() {
    return ResponseEntity.ok(
      offeringDAO.get()
    );
  }
}
