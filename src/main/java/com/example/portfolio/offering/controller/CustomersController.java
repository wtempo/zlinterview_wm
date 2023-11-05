package com.example.portfolio.offering.controller;

import com.example.portfolio.offering.read.CustomerDAO;
import com.example.portfolio.offering.read.dto.CustomerDTO;
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
@RequestMapping("/customers")
public class CustomersController {
  private final CustomerDAO customerDAO;

  @GetMapping(value = "/")
  public ResponseEntity<List<CustomerDTO>> getCustomers() {
    return ResponseEntity.ok(
      customerDAO.get()
    );
  }

  @GetMapping(value = "/{customer_id}")
  public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("customer_id") UUID customerId) {
    return ResponseEntity.ok(
      customerDAO.getSingle(customerId)
    );
  }
}
