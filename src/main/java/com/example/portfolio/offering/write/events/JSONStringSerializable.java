package com.example.portfolio.offering.write.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public interface JSONStringSerializable {
  default String toJsonString() throws JsonProcessingException {
    return getJsonMapper().writeValueAsString(this);
  }

  private JsonMapper getJsonMapper() {
    return JsonMapper
      .builder()
      .findAndAddModules()
      .build();
  }
}
