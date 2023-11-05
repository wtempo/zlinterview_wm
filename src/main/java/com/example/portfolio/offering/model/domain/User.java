package com.example.portfolio.offering.model.domain;

import com.example.portfolio.offering.exception.UserNotAssociatedWithCustomerException;
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
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  private Customer customer;

  private String firstName;
  private String lastName;

  public Subscription subscribeToOffering(
    CustomerOffering customerOffering
  ) {
    if (!belongsToSameCustomer(customerOffering)) {
      throw new UserNotAssociatedWithCustomerException(id, customer.getId());
    }

    customerOffering.throwIfSubscriptionLimitReached();

    return Subscription.newSubscriptionForUserId(customerOffering, this.id);
  }

  public boolean belongsToSameCustomer(CustomerOffering customerOffering) {
    return this.getCustomer().getId() == customerOffering.getCustomer().getId();
  }
}
