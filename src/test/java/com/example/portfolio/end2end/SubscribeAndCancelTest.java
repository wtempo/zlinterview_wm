package com.example.portfolio.end2end;

import com.example.portfolio.End2EndTest;
import com.example.portfolio.offering.read.dto.CustomerDTO;
import com.example.portfolio.offering.read.dto.SubscriptionDTO;
import com.example.portfolio.offering.write.dto.CreateSubscriptionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@End2EndTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/e2e-blueprints/blueprint.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/e2e-blueprints/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SubscribeAndCancelTest {
  @LocalServerPort
  private Integer port;

  @Autowired
  PostgreSQLContainer<?> postgreSQLContainer;

  @Autowired
  private TestRestTemplate restTemplate;

  UUID expectedBlueprintCustomerId = UUID.fromString("1ef3f80a-7c29-11ee-b962-0242ac120002");
  UUID expectedBlueprintOfferingId = UUID.fromString("7b8488bd-bbeb-4e23-9d7a-246b71e02911");

  @Test
    // Smoke test to confirm relevant initial conditions and the endpoint being alive
  void whenInitialConditions_getCustomers_returnsInitialData() throws Exception {
    var getCustomersResponse = getCustomersResponse();
    assertThat(getCustomersResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(getCustomersResponse.getBody().size()).isEqualTo(1);
    assertThat(getCustomersResponse.getBody().get(0).getId()).isEqualTo(expectedBlueprintCustomerId);

    var getSingleCustomerResponse = getSingleCustomerResponse(expectedBlueprintCustomerId);

    assertThat(getSingleCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertOnAvailableOfferingsCounts(getSingleCustomerResponse.getBody(), 3, 0);

    var availableOfferings = getSingleCustomerResponse.getBody().getAvailableOfferings();
    assertThat(availableOfferings.size()).isEqualTo(1);
    assertThat(availableOfferings.get(0).getOfferingId()).isEqualTo(expectedBlueprintOfferingId);
  }

  @Test
  void whenSubscriptionsCreated_getCustomers_returnsUpdatedCountersButDoesNotAllowTheCounterToExceedMax() throws Exception {
    var singleCustomer = getSingleCustomerResponse(expectedBlueprintCustomerId)
      .getBody();

    var blueprintUserIds = List.of(
      UUID.fromString("aaad212c-b879-47ae-b1de-a8ed130d4573"),
      UUID.fromString("bbb2ce04-9ca9-4288-8880-ce56cfd1749f"),
      UUID.fromString("cccff9dc-1649-4487-b9c6-033b7a30765d")
    );

    var createdSubscriptionIds = blueprintUserIds.stream().map(userId -> {
      var response = subscribe(
        CreateSubscriptionDTO
          .builder()
          .customerId(expectedBlueprintCustomerId)
          .offeringId(expectedBlueprintOfferingId)
          .userId(userId)
          .build()
      );

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      assertThat(response.getBody()).isNotNull();

      return response.getBody().getId();
    }).collect(Collectors.toSet());

    var singleCustomerAfterSubscribing = getSingleCustomerResponse(expectedBlueprintCustomerId)
      .getBody();

    assertOnAvailableOfferingsCounts(singleCustomerAfterSubscribing, 3, 3);

    var response = subscribe(
      CreateSubscriptionDTO
        .builder()
        .customerId(expectedBlueprintCustomerId)
        .offeringId(expectedBlueprintOfferingId)
        .userId(UUID.fromString("dddaaadc-1649-4487-b9c6-033b7a30765d"))
        .build()
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

    var singleCustomerAfterExceedingSubscriptions = getSingleCustomerResponse(expectedBlueprintCustomerId)
      .getBody();

    assertOnAvailableOfferingsCounts(singleCustomerAfterExceedingSubscriptions, 3, 3);

    createdSubscriptionIds.forEach((subscriptionId) -> {
      var deleteResponse = unsubscribe(subscriptionId);
      assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    });

    var singleCustomerAfterDeleteSubscriptions = getSingleCustomerResponse(expectedBlueprintCustomerId)
      .getBody();
    assertOnAvailableOfferingsCounts(singleCustomerAfterDeleteSubscriptions, 3, 0);
  }

  private void assertOnAvailableOfferingsCounts(CustomerDTO customerDTO, int expectedMax, int expectedActive) {
    assertThat(customerDTO).isNotNull();
    assertThat(customerDTO.getAvailableOfferings().get(0).getActiveSubscriberLimit()).isEqualTo(expectedMax);
    assertThat(customerDTO.getAvailableOfferings().get(0).getActiveSubscriberCount()).isEqualTo(expectedActive);
  }

  private ResponseEntity<SubscriptionDTO> subscribe(CreateSubscriptionDTO createSubscriptionDTO) {
    var uri = UriComponentsBuilder
      .fromHttpUrl(
        String.format("http://127.0.0.1:%s/subscriptions", port)
      )
      .build()
      .toUri();

    return this
      .restTemplate
      .exchange(
        uri,
        HttpMethod.POST,
        new HttpEntity<>(createSubscriptionDTO),
        new ParameterizedTypeReference<>() {
        });
  }

  private ResponseEntity<Void> unsubscribe(UUID subscriptionId) {
    var uri = UriComponentsBuilder
      .fromHttpUrl(
        String.format("http://127.0.0.1:%s/subscriptions/%s", port, subscriptionId)
      )
      .build()
      .toUri();

    return this
      .restTemplate
      .exchange(
        uri,
        HttpMethod.DELETE,
        HttpEntity.EMPTY,
        new ParameterizedTypeReference<>() {
        });
  }

  private ResponseEntity<List<CustomerDTO>> getCustomersResponse() {
    var uri = UriComponentsBuilder
      .fromHttpUrl(
        String.format("http://127.0.0.1:%s/customers/", port)
      )
      .build()
      .toUri();

    return this
      .restTemplate
      .exchange(
        uri,
        HttpMethod.GET,
        HttpEntity.EMPTY,
        new ParameterizedTypeReference<>() {
        });
  }

  private ResponseEntity<CustomerDTO> getSingleCustomerResponse(UUID customerId) {
    var uri = UriComponentsBuilder
      .fromHttpUrl(
        String.format("http://127.0.0.1:%s/customers/%s", port, customerId)
      )
      .build()
      .toUri();

    return this
      .restTemplate
      .exchange(
        uri,
        HttpMethod.GET,
        HttpEntity.EMPTY,
        new ParameterizedTypeReference<>() {
        });
  }
}
