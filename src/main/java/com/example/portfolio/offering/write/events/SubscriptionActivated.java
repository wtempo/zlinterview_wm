package com.example.portfolio.offering.write.events;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionActivated(
  UUID userId,
  UUID offeringId,

  UUID customerId,
  Instant activatedAt
) implements Payload {}
