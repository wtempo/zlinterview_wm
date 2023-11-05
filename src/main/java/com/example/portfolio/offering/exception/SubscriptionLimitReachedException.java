package com.example.portfolio.offering.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Subscription limit has been reached")

public class SubscriptionLimitReachedException extends RuntimeException {
  public SubscriptionLimitReachedException(long limit, UUID customerId, UUID offeringId) {
    super(
      String.format(
        "Subscription limit of %d has been reached by the Customer [id=%s]  for Offering [id=%s]",
        limit,
        customerId,
        offeringId
      )
    );
  }
}
