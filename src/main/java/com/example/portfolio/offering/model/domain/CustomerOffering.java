package com.example.portfolio.offering.model.domain;

import com.example.portfolio.offering.exception.SubscriptionLimitReachedException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOffering {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  private Customer customer;

  @ManyToOne
  private Offering offering;

  private long activeSubscriptionLimit;

  @OneToMany(mappedBy = "customerOffering")
  @Where(clause = "active = true")
  @Builder.Default
  private Set<Subscription> activeSubscriptions = new HashSet<>();

  public List<UUID> getSKUs() {
    return getOffering().getSKUs();
  }

  public void addSubscription(Subscription subscription) {
    if (subscription.getActive()) {
      throwIfSubscriptionLimitReached();
      activeSubscriptions.add(subscription);
    }
  }

  private void throwIfSubscriptionLimitReached() throws SubscriptionLimitReachedException {
    if (isSubscriptionLimitReached()) {
      throw new SubscriptionLimitReachedException(activeSubscriptionLimit, customer.getId(), offering.getId());
    }
  }

  public boolean isSubscriptionLimitReached() {
    return activeSubscriptions.size() >= activeSubscriptionLimit;
  }
}
