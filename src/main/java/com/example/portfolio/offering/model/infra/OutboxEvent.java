package com.example.portfolio.offering.model.infra;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(columnList = "createdAt, status")) // TODO: just for now
public class OutboxEvent {
  @Id
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  @Column
  private Status status = Status.PENDING;

  private String type;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private JsonNode payload;

  @CreationTimestamp
  @Column(nullable = false) // TODO: remove
  private Instant createdAt;

  private Instant sentAt;

  public enum Status {
    PENDING, SENT
  }
  public void markAsSent() {
    this.sentAt = Instant.now();
    this.status = Status.SENT;
  }
}
