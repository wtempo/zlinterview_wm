package com.example.portfolio.offering.service.transactionaloutbox.transport;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface EventOutboundGateway {
  void send(String message) throws JsonProcessingException;
}
