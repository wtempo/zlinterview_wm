package com.example.portfolio.offering.write.events;

import com.example.portfolio.offering.model.infra.OutboxEvent;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EventEnvelopeDTO implements JSONStringSerializable {
  private UUID id;
  private String type;
  private JsonNode payload;
  private Instant createdAt;

  public static EventEnvelopeDTO of(OutboxEvent outboxEvent) {
    return builder()
      .id(outboxEvent.getId())
      .type(outboxEvent.getType())
      .payload(outboxEvent.getPayload())
      .createdAt(Instant.now())
      .build();
  }
}
