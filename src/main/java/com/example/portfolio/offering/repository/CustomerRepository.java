package com.example.portfolio.offering.repository;

import com.example.portfolio.offering.model.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface CustomerRepository extends JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {
  @Override
  // A solution for N+1 queries, could also be solved with e.g. entity graph or custom query.
  @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.customerOfferings co LEFT JOIN FETCH co.offering")
  List<Customer> findAll();
}
