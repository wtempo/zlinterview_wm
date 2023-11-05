package com.example.portfolio.offering.write;

import com.example.portfolio.offering.exception.EntityNotFoundException;
import com.example.portfolio.offering.repository.CustomerOfferingRepository;
import com.example.portfolio.offering.repository.SubscriptionRepository;
import com.example.portfolio.offering.repository.UserRepository;
import com.example.portfolio.offering.service.transactionaloutbox.OutboxService;
import com.example.portfolio.offering.write.dto.CreateSubscriptionDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SubscriptionWriteDAO {
  private final OutboxService outboxService;
  private final UserRepository userRepository;
  private final SubscriptionRepository subscriptionRepository;
  private final CustomerOfferingRepository customerOfferingRepository;

  @Transactional
  public UUID subscribeToOffering(CreateSubscriptionDTO createSubscriptionDTO) {
    var user = userRepository
      .findById(createSubscriptionDTO.getUserId())
      .orElseThrow();

    // We lock here to make sure we can perform validations such as
    // checking the subscription count and no other transaction will
    // create subscriptions at the same time.
    var customerOffering = customerOfferingRepository.findByCustomerIdAndServiceIdWithLock(
      createSubscriptionDTO.getCustomerId(),
      createSubscriptionDTO.getOfferingId()
    ).orElseThrow(EntityNotFoundException::new);

    var newSubscription = user.subscribeToOffering(customerOffering);

    outboxService.publishSubscriptionActivated(newSubscription);

    return subscriptionRepository.save(newSubscription).getId();
  }

  @Transactional
  public void cancelSubscription(UUID subscriptionId) {
    var subscription = subscriptionRepository
      .findById(subscriptionId)
      .orElseThrow(EntityNotFoundException::new);

    subscription.cancel();

    outboxService.publishSubscriptionCanceled(subscription);
  }
}
