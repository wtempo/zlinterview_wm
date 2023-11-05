package com.example.portfolio.offering.read.datafetcher;

import com.example.portfolio.offering.read.dto.SkuDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SKUBatchFetcher {
  private final Environment environment;

  @Async
  public CompletableFuture<Map<UUID, SkuDTO>> fetch(Set<UUID> keys) {
    return CompletableFuture.completedFuture(fetchNow(keys));
  }

  public Map<UUID, SkuDTO> fetchNow(Set<UUID> keys) {
    var port = environment.getProperty("local.server.port");
    var uri = UriComponentsBuilder
      .fromHttpUrl(
        String.format("http://127.0.0.1:%s", port)
      )
      .path("/skus")
      .queryParam("sku_ids", keys)
      .build()
      .toUri();

    RestTemplate restTemplate = new RestTemplate();
    var response = restTemplate.exchange(
      uri,
      HttpMethod.GET,
      HttpEntity.EMPTY,
      new ParameterizedTypeReference<List<SkuDTO>>() {
      }
    ).getBody();

    return Optional
      .ofNullable(response)
      .map(listOfSKUs -> listOfSKUs.stream().collect(Collectors.toMap(
        SkuDTO::getId,
        Function.identity()
      )))
      .orElseThrow();
  }
}
