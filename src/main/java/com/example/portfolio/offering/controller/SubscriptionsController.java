package com.example.portfolio.offering.controller;

import com.example.portfolio.offering.read.SubscriptionDAO;
import com.example.portfolio.offering.read.dto.SubscriptionDTO;
import com.example.portfolio.offering.write.SubscriptionWriteDAO;
import com.example.portfolio.offering.write.dto.CreateSubscriptionDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionsController {
  private final SubscriptionDAO subscriptionDAO;
  private final SubscriptionWriteDAO subscriptionWriteDAO;

  @GetMapping(value = "/{subscription_id}")
  public ResponseEntity<SubscriptionDTO> getOffering(@PathVariable("subscription_id") UUID subscriptionId) {
    return ResponseEntity.ok(
      subscriptionDAO.getSingle(subscriptionId)
    );
  }

  @GetMapping(value = "/")
  public ResponseEntity<List<SubscriptionDTO>> getOfferings() {
    return ResponseEntity.ok(
      subscriptionDAO.get()
    );
  }

  @PostMapping
  public ResponseEntity<SubscriptionDTO> createSubscription(@RequestBody CreateSubscriptionDTO createSubscriptionDTO) {
    var subscriptionId = subscriptionWriteDAO.subscribeToOffering(createSubscriptionDTO);
    return ResponseEntity.status(201).body(subscriptionDAO.getSingle(subscriptionId));
  }

  @DeleteMapping("/{subscription_id}")
  public ResponseEntity<Void> deleteSubscription(@PathVariable("subscription_id") UUID subscriptionId) {
    subscriptionWriteDAO.cancelSubscription(subscriptionId);
    return ResponseEntity.ok().build();
  }
}
