package com.example.portfolio.offering.write.events;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionCanceled(
  UUID userId,
  UUID offeringId,
  UUID customerId,
  Instant deactivatedAt
) implements Payload {}
