package com.example.portfolio.offering.service.transactionaloutbox.transport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Builder
@AllArgsConstructor
public class HttpEventOutboundGateway implements EventOutboundGateway {
  private final URI uri;

  @Override
  public void send(String message) {
    RestTemplate restTemplate = new RestTemplate();

    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> request = new HttpEntity<>(message, headers);

    // For illustration purpose only.
    // We don't care about response too much, as long as it doesn't throw it's fine.
    // If it was some async mechanism such as queue we wouldn't get it anyway.
    restTemplate.postForEntity(
      uri,
      request,
      String.class
    );
  }
}
