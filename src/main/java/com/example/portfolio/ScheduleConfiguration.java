package com.example.portfolio;

import com.example.portfolio.offering.service.transactionaloutbox.MessageRelay;
import jakarta.persistence.PessimisticLockException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@AllArgsConstructor
public class ScheduleConfiguration {
  private final MessageRelay transactionalOutboxMessageRelay;

  @Scheduled(fixedDelay = 5000)
  public void poll() {
    try {
      transactionalOutboxMessageRelay.pollAndSend();
    } catch (PessimisticLockException pessimisticLockException) {
      log.trace("Message relay batch locked. Skipping.");
    }
  }
}
