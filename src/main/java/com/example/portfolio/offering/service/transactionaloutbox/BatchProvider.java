package com.example.portfolio.offering.service.transactionaloutbox;

import com.example.portfolio.offering.model.infra.OutboxEvent;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BatchProvider {
  private final EntityManager entityManager;

  @Transactional(propagation = Propagation.MANDATORY)
  public List<OutboxEvent> getInOrder(int batchSize) {
   return fetchEvents(batchSize, LockMode.UPGRADE_NOWAIT);
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public List<OutboxEvent> getParallelBatch(int batchSize) {
    return fetchEvents(batchSize, LockMode.UPGRADE_SKIPLOCKED);
  }

  private List<OutboxEvent> fetchEvents(int batchSize, LockMode lockMode) {
    // unwrapping to get the hibernate session to use LockOptions as LockMode.UPGRADE_SKIPLOCKED got deprecated
    return entityManager
      .unwrap(Session.class)
      .createQuery(
        "SELECT ev FROM OutboxEvent ev WHERE ev.status = Status.PENDING ORDER BY createdAt",
        OutboxEvent.class
      )
      .setMaxResults(batchSize)
      .setLockOptions(new LockOptions(lockMode))
      .getResultList();
  }
}
