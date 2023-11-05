package com.example.portfolio.offering.service.transactionaloutbox;

import com.example.portfolio.offering.exception.OutboxSendFailureException;
import com.example.portfolio.offering.model.infra.OutboxEvent;
import com.example.portfolio.offering.service.transactionaloutbox.transport.EventOutboundGateway;
import com.example.portfolio.offering.write.events.EventEnvelopeDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class MessageRelay {
  private final BatchProvider messageBatchProvider;
  private final EventOutboundGateway eventOutboundGateway;

  @Transactional
  public void pollAndSend() {
    var batch = messageBatchProvider.getInOrder(25);

    try {
      for (OutboxEvent event : batch) {
        sendEvent(event);
      }
    } catch (OutboxSendFailureException exception) {
      // We catch exception and interrupt sending the batch
      // to not send further events out of order.
      // We expect at-least-once delivery, so resending part of a batch
      // is fine. It implies the downstream services must be idempotent.
      log.error("Batch processing interrupted.", exception);
    }
  }

  private void sendEvent(OutboxEvent event) throws OutboxSendFailureException {
    try {
      var message = EventEnvelopeDTO.of(event).toJsonString();
      eventOutboundGateway.send(message);
      event.markAsSent();
    } catch (Exception exception) {
      // Log for visibility and rethrow
      throw new OutboxSendFailureException(event.getId(), exception);
    }
  }
}
