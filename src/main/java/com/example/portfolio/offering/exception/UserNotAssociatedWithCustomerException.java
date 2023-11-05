package com.example.portfolio.offering.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found")
public class UserNotAssociatedWithCustomerException extends RuntimeException {
  public UserNotAssociatedWithCustomerException(UUID userId, UUID customerId) {
    super(
      String.format(
        "User [id=%s] not found for the Customer [id=%s]",
        userId,
        customerId
      )
    );
  }
}
