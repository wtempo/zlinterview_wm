package com.example.portfolio.offering.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Downstream service unavailable")
public class BillingServiceUnavailableException extends RuntimeException {
  public BillingServiceUnavailableException(Throwable cause) {
    super(cause);
  }
}
