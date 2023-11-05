package com.example.portfolio.offering.service.transactionaloutbox.transport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class EventOutboundGatewayProvider {
  @Value("${server.port}")
  private int port;

  @Bean
  public EventOutboundGateway httpEventSender() {
    var uri = UriComponentsBuilder
      .fromHttpUrl(
        String.format("http://127.0.0.1:%s", port)
      )
      .path("/billing/events")
      .build()
      .toUri();

    return HttpEventOutboundGateway
      .builder()
      .uri(uri)
      .build();
  }
}
