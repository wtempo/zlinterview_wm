package com.example.portfolio.offering.read;

import com.example.portfolio.offering.exception.BillingServiceUnavailableException;
import com.example.portfolio.offering.model.domain.Customer;
import com.example.portfolio.offering.model.domain.CustomerOffering;
import com.example.portfolio.offering.read.datafetcher.SKUBatchFetcher;
import com.example.portfolio.offering.read.datafetcher.SubscriptionCountBatchFetcher;
import com.example.portfolio.offering.read.dto.AvailableOfferingDTO;
import com.example.portfolio.offering.read.dto.CustomerDTO;
import com.example.portfolio.offering.read.dto.SkuDTO;
import com.example.portfolio.offering.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerDAO implements DAO<UUID, CustomerDTO> {
  private final SKUBatchFetcher skuBatchFetcher;
  private final CustomerRepository customerRepository;
  private final SubscriptionCountBatchFetcher subscriptionCountFetcher;

  @Override
  public CustomerDTO getSingle(UUID customerId) {
    var customer = customerRepository
      .findById(customerId)
      .orElseThrow();

    return convertToDTO(List.of(customer)).get(0);
  }

  @Override
  public List<CustomerDTO> get() {
    var customers = customerRepository.findAll();
    return convertToDTO(customers);
  }

  private List<CustomerDTO> convertToDTO(Collection<Customer> customers) {
    var activeSubscriptionFuture = asyncFetchSubscriptionCountsPerCustomerOfferingId(customers);
    var skuPerIdFuture = asyncFetchSKUs(customers);

    try {
      waitForCompletion(activeSubscriptionFuture, skuPerIdFuture);
      var activeSubscriptionCountsPerCustomerOfferingIdMap = activeSubscriptionFuture.get();
      var skuPerIdMap = skuPerIdFuture.get();
      return buildCustomerDTOs(customers, activeSubscriptionCountsPerCustomerOfferingIdMap, skuPerIdMap);
    } catch (InterruptedException | ExecutionException | TimeoutException ex) {
      log.error("ZONK", ex);
      throw new BillingServiceUnavailableException(ex);
    }
  }

  private List<SkuDTO> buildSKUSDTO(Map<UUID, SkuDTO> skus, CustomerOffering customerOffering) {
    return customerOffering.getSKUs().stream().map(skus::get).collect(Collectors.toList());
  }

  private void waitForCompletion(CompletableFuture<?>... cfs) throws ExecutionException, InterruptedException, TimeoutException {
    // Could be configurable globally or per service etc.
    CompletableFuture.allOf(cfs).get(1000, TimeUnit.MILLISECONDS);
  }

  private List<CustomerDTO> buildCustomerDTOs(Collection<Customer> customers, Map<UUID, Long> subscriptionCounts, Map<UUID, SkuDTO> skus) {
    return customers.stream().map(customer ->
      CustomerDTO.of(
        customer,
        buildAvailableOfferingsDTO(subscriptionCounts, skus, customer)
      )
    ).collect(Collectors.toList());
  }

  private List<AvailableOfferingDTO> buildAvailableOfferingsDTO(Map<UUID, Long> subscriptionCounts, Map<UUID, SkuDTO> skus, Customer customer) {
    return customer
      .getCustomerOfferings()
      .stream()
      .map(customerOffering ->
        AvailableOfferingDTO
          .of(
            customerOffering,
            subscriptionCounts.get(customerOffering.getId()),
            buildSKUSDTO(skus, customerOffering)
          )
      ).collect(Collectors.toList());
  }

  private CompletableFuture<Map<UUID, SkuDTO>> asyncFetchSKUs(Collection<Customer> customers) {
    var skuIds = customers
      .stream()
      .flatMap(customer -> customer.getCustomerOfferings().stream())
      .flatMap(customerOffering -> customerOffering.getSKUs().stream())
      .collect(Collectors.toSet());

    return skuBatchFetcher.fetch(skuIds);
  }

  private CompletableFuture<Map<UUID, Long>> asyncFetchSubscriptionCountsPerCustomerOfferingId(Collection<Customer> customers) {
    var customerOfferingIds = customers
      .stream()
      .flatMap(Customer::getCustomerOfferingIdsStream)
      .collect(Collectors.toSet());

    return subscriptionCountFetcher.fetch(
      customerOfferingIds
    );
  }
}
