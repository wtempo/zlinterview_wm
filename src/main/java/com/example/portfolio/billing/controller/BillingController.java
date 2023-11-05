package com.example.portfolio.billing.controller;

import com.example.portfolio.billing.read.CustomerBillingService;
import com.example.portfolio.billing.read.CustomerBillingSummary;
import com.example.portfolio.billing.write.BillingEventService;
import com.example.portfolio.offering.write.events.EventEnvelopeDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/billing")
public class BillingController {
  private final BillingEventService billingEventService;
  private final CustomerBillingService customerBillingService;

  @PostMapping("/events")
  public ResponseEntity<String> postEvent(@RequestBody EventEnvelopeDTO envelopeDTO) {
    billingEventService.createByIncomingEvent(envelopeDTO);
    return ResponseEntity.ok("OK");
  }

  @GetMapping("/customer/{customer_id}/summary")
  public ResponseEntity<CustomerBillingSummary> getCustomerBillingSummary(@PathVariable("customer_id") UUID customerId) {
    return ResponseEntity.ok(
      customerBillingService.getCustomerBillingSummary(customerId)
    );
  }
}
