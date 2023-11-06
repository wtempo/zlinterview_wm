package com.example.portfolio.offering.model.domain;

import com.example.portfolio.offering.exception.SubscriptionLimitReachedException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {
  @Test
  void subscribeToOffering_whenSubscriptionLimitNotReached_Works() {
    var customer = buildCustomer("name");
    var user = buildUser(customer);
    var customerOffering = buildCustomerOffering(customer);

    var newSubscription = user.subscribeToOffering(customerOffering);

    assertThat(newSubscription).isNotNull();
  }

  @Test
  void subscribeToOffering_whenSubscriptionLimitReached_Throws() {
    var customer = buildCustomer("name");
    var user1 = buildUser(customer);
    var user2 = buildUser(customer);
    var customerOffering = buildCustomerOffering(customer);

    var newSubscription = user1.subscribeToOffering(customerOffering);
    assertThat(newSubscription).isNotNull();

    assertThrows(SubscriptionLimitReachedException.class, () -> user2.subscribeToOffering(customerOffering));
  }

  @Test
  void belongsToSameCustomer_whenBothEntitiesHaveCustomerWithSameId_ReturnsTrue() {
    var customer = buildCustomer("name");
    var user = buildUser(customer);
    var customerOffering = buildCustomerOffering(customer);

    assertThat(user.belongsToSameCustomer(customerOffering)).isTrue();
  }

  @Test
  void belongsToSameCustomer_whenBothEntitiesHaveCustomerWithDifferentId_ReturnsFalse() {
    var customer = buildCustomer("name");
    var otherCustomer = buildCustomer("name2");

    var user = buildUser(customer);
    var customerOffering = buildCustomerOffering(otherCustomer);

    assertThat(user.belongsToSameCustomer(customerOffering)).isFalse();
  }

  private static User buildUser(Customer customer) {
    return User
      .builder()
      .customer(customer)
      .build();
  }

  private static CustomerOffering buildCustomerOffering(Customer customer) {
    return CustomerOffering
      .builder()
      .customer(customer)
      .offering(
        Offering.builder().id(UUID.randomUUID()).build()
      )
      .activeSubscriptionLimit(1)
      .build();
  }

  private static Customer buildCustomer(String name) {
    return Customer
      .builder()
      .id(UUID.randomUUID())
      .name(name)
      .build();
  }
}
