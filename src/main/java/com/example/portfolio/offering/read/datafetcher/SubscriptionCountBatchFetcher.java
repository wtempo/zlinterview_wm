package com.example.portfolio.offering.read.datafetcher;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionCountBatchFetcher {
  private final EntityManager entityManager;

  @Async
  @Transactional
  public CompletableFuture<Map<UUID, Long>> fetch(Set<UUID> keys) {
    return CompletableFuture.completedFuture(fetchNow(keys));
  }

  public Map<UUID, Long> fetchNow(Set<UUID> keys) {
    var results = entityManager
      .createQuery("""
        SELECT co.id AS id, COUNT(s.id) AS count
        FROM CustomerOffering co
        LEFT JOIN co.activeSubscriptions s
        GROUP BY co.id""",
        Tuple.class
      ).getResultStream();

    return results.collect(
      Collectors.toMap(
        tuple -> tuple.get("id", UUID.class),
        tuple -> tuple.get("count", Long.class)
      )
    );
  }
}
