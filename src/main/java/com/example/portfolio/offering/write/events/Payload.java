package com.example.portfolio.offering.write.events;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

// This is a simple JSON serialization / deserialization for illustration purpose
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "payloadType")
public interface Payload {
  default JsonNode toJsonNode() {
    return getJsonMapper().convertValue(this, JsonNode.class);
  }

  static Payload fromJsonNode(JsonNode node) throws JsonProcessingException {
    return getJsonMapper().treeToValue(node, Payload.class);
  }

  private static JsonMapper getJsonMapper() {
    return JsonMapper
      .builder()
      .findAndAddModules()
      .build();
  }
}
