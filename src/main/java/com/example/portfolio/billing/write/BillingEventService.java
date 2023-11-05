package com.example.portfolio.billing.write;

import com.example.portfolio.billing.model.BillingEvent;
import com.example.portfolio.billing.repository.BillingEventRepository;
import com.example.portfolio.offering.write.events.EventEnvelopeDTO;
import com.example.portfolio.offering.write.events.Payload;
import com.example.portfolio.offering.write.events.SubscriptionActivated;
import com.example.portfolio.offering.write.events.SubscriptionCanceled;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class BillingEventService {
  private final BillingEventRepository billingEventRepository;

  @Transactional
  public void createByIncomingEvent(EventEnvelopeDTO envelopeDTO) {
    // Let's make it idempotent. Could also be a unique index on correlationId and
    // 'on duplicate key ignore' / ON CONFLICT clause.
    if (billingEventRepository.countByCorrelationId(envelopeDTO.getId()) > 0) {
      log.info(
        String.format("Duplicated event [id=%s] received by %s and ignored.", envelopeDTO.getId(), getClass().getSimpleName())
      );
      return;
    }

    try {
      var deserializedPayload = Payload.fromJsonNode(envelopeDTO.getPayload());

      // For illustration purpose only. Could be a double dispatch pattern,
      // a bunch of ifs with instanceof or simply classes to which it would deserialize
      // could implement methods we need etc.
      // In future e.g.
      //       switch (payload) {
      //        case SubscriptionActivated subscriptionActivated -> doSomething();
      //        case SubscriptionDeactivated subscriptionDeactivated -> doSomethingElse();
      //        default -> doDefault();
      //      };
      // seems to be interesting.

      if (deserializedPayload instanceof SubscriptionActivated payload) {
        handle(envelopeDTO.getId(), payload);
      } else if (deserializedPayload instanceof SubscriptionCanceled payload) {
        handle(envelopeDTO.getId(), payload);
      } else {
        log.warn("Unknown event type received by billing service.");
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private void handle(UUID correlationId, SubscriptionActivated subscriptionActivated) {
    var event = BillingEvent
      .builder()
      .eventTime(subscriptionActivated.activatedAt())
      .eventType("BILLING START")
      .userId(subscriptionActivated.userId())
      .offeringId(subscriptionActivated.offeringId())
      .correlationId(correlationId)
      .build();

    billingEventRepository.save(event);
  }

  private void handle(UUID correlationId, SubscriptionCanceled subscriptionDeactivated) {
    var event = BillingEvent
      .builder()
      .eventTime(subscriptionDeactivated.deactivatedAt())
      .eventType("BILLING END")
      .userId(subscriptionDeactivated.userId())
      .offeringId(subscriptionDeactivated.offeringId())
      .correlationId(correlationId)
      .build();

    billingEventRepository.save(event);
  }
}
