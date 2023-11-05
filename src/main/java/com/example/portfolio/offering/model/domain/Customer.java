package com.example.portfolio.offering.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  @OneToMany(mappedBy = "customer")
  private Set<CustomerOffering> customerOfferings;

  public Stream<UUID> getCustomerOfferingIdsStream() {
    return customerOfferings.stream().map(CustomerOffering::getId);
  }
}
