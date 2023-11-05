package com.example.portfolio.billing.repository;

import com.example.portfolio.billing.model.BillingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface BillingEventRepository extends JpaRepository<BillingEvent, UUID> {
  Long countByCorrelationId(@Param("correlation_id") UUID correlationId);
}
