package com.example.portfolio.offering.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private UUID userId;

  @Column(name = "active")
  private Boolean active;

  @ManyToOne
  private Customer customer;

  @ManyToOne
  private Offering offering;

  @ManyToOne
  private CustomerOffering customerOffering;

  public static Subscription newSubscriptionForUserId(CustomerOffering customerOffering, UUID userId) {
   return builder()
      .customerOffering(customerOffering)
      .customer(customerOffering.getCustomer())
      .offering(customerOffering.getOffering())
      .active(true)
      .userId(userId)
      .build();
  }

  public void cancel() {
    this.active = false;
  }
}
