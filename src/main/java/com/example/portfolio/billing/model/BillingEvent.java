package com.example.portfolio.billing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private UUID correlationId;
  private UUID userId;
  private UUID offeringId;
  private UUID customerId;
  private Instant eventTime;

  @Enumerated(EnumType.STRING)
  private EventType eventType;

  public enum EventType {
    START,
    END
  }

}
