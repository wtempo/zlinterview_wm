package com.example.portfolio.offering.exception;

import java.util.UUID;

public class OutboxSendFailureException extends Exception {
  public OutboxSendFailureException(UUID outboxEventId, Throwable cause) {
    super(
      String.format(
        "Unable to send outbox message for outbox event [id=%s]",
        outboxEventId
      ),
      cause
    );
  }
}
