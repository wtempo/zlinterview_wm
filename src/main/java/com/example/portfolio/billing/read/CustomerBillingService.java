package com.example.portfolio.billing.read;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerBillingService {
  public final EntityManager entityManager;

  // A toy, not tested query to play with the data.
  // We don't validate the state of subscription in this service, so if we received
  // doubled, not deduplicated SubscriptionActivated event we'd double count the same period.
  public CustomerBillingSummary getCustomerBillingSummary(UUID customerId) {
    var result = entityManager
      .createNativeQuery("""
         SELECT SUM(seconds_between) as seconds, customer_id
         FROM (SELECT EXTRACT(EPOCH FROM (COALESCE(end_event.event_time, now()) - start_event.event_time)) as seconds_between,
                      start_event.id,
                      end_event.id,
                      start_event.customer_id,
                      start_event.user_id,
                      start_event.offering_id,
                      end_event.id IS NULL as active
               FROM billing_event start_event LEFT JOIN LATERAL (
                   SELECT *
                   FROM billing_event end_event
                   WHERE event_type = :end_type
                     AND end_event.event_time > start_event.event_time
                     AND end_event.customer_id = start_event.customer_id
                     AND end_event.offering_id = start_event.offering_id
                     AND end_event.user_id = start_event.user_id
                   ORDER BY event_time
                   LIMIT 1
                ) end_event
               ON true
               WHERE start_event.customer_id = :customer_id
                 AND start_event.event_type = :start_type) pre_aggregated
         GROUP BY customer_id;
        """, Tuple.class)
      .setParameter("customer_id", customerId)
      .setParameter("start_type", "BILLING START")
      .setParameter("end_type", "BILLING END")
      .getSingleResult();

    Tuple resultTuple = (Tuple) result;

    return new CustomerBillingSummary(
      resultTuple.get("customer_id", UUID.class),
      resultTuple.get("seconds", BigDecimal.class)
    );
  }
}
