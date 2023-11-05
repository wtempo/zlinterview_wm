package com.example.portfolio.offering.read.dto;

import com.example.portfolio.offering.model.domain.Customer;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CustomerDTO {
  private UUID id;
  private String name;
  private List<AvailableOfferingDTO> availableOfferings;

  public static CustomerDTO of(Customer customer, List<AvailableOfferingDTO> availableOfferings) {
    return CustomerDTO
      .builder()
      .id(customer.getId())
      .name(customer.getName())
      .availableOfferings(availableOfferings)
      .build();
  }
}
