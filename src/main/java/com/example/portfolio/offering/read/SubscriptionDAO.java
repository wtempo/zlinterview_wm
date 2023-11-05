package com.example.portfolio.offering.read;

import com.example.portfolio.offering.model.domain.Subscription;
import com.example.portfolio.offering.read.dto.SubscriptionDTO;
import com.example.portfolio.offering.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionDAO implements DAO<UUID, SubscriptionDTO> {
  private final SubscriptionRepository subscriptionRepository;

  @Override
  public SubscriptionDTO getSingle(UUID subscriptionId) {
    var subscription = subscriptionRepository
      .findById(subscriptionId)
      .orElseThrow();

    return convertToDTO(List.of(subscription)).get(0);
  }

  @Override
  public List<SubscriptionDTO> get() {
    var subscriptions = subscriptionRepository
      .findAll();

    return convertToDTO(subscriptions);
  }

  private List<SubscriptionDTO> convertToDTO(Collection<Subscription> entities) {
    return entities.stream().map(SubscriptionDTO::of).collect(Collectors.toList());
  }
}
