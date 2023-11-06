package com.example.portfolio.offering.model.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserTest {
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
