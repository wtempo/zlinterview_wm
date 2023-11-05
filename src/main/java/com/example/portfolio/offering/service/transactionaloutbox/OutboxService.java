package com.example.portfolio.offering.service.transactionaloutbox;

import com.example.portfolio.offering.model.domain.Subscription;
import com.example.portfolio.offering.model.infra.OutboxEvent;
import com.example.portfolio.offering.repository.EventRepository;
import com.example.portfolio.offering.write.events.Payload;
import com.example.portfolio.offering.write.events.SubscriptionActivated;
import com.example.portfolio.offering.write.events.SubscriptionCanceled;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OutboxService {
  private final EventRepository eventRepository;

  @Transactional(propagation = Propagation.MANDATORY)
  public OutboxEvent publishSubscriptionActivated(Subscription subscription) {
    var payload = new SubscriptionActivated(
      subscription.getUserId(),
      subscription.getOffering().getId(),
      subscription.getCustomer().getId(),
      Instant.now()
    );

    OutboxEvent event = buildEventByPayload(payload);

    return eventRepository.save(event);
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public OutboxEvent publishSubscriptionCanceled(Subscription subscription) {
    var payload = new SubscriptionCanceled(
      subscription.getUserId(),
      subscription.getOffering().getId(),
      subscription.getCustomer().getId(),
      Instant.now()
    );

    OutboxEvent event = buildEventByPayload(payload);

    return eventRepository.save(event);
  }

  private static OutboxEvent buildEventByPayload(Payload payload) {
    return OutboxEvent
      .builder()
      .id(UUID.randomUUID())
      .type(payload.getClass().getSimpleName())
      .payload(payload.toJsonNode())
      .build();
  }
}
