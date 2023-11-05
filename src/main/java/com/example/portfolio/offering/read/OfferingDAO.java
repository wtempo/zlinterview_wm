package com.example.portfolio.offering.read;

import com.example.portfolio.offering.exception.BillingServiceUnavailableException;
import com.example.portfolio.offering.model.domain.Offering;
import com.example.portfolio.offering.read.datafetcher.SKUBatchFetcher;
import com.example.portfolio.offering.read.dto.OfferingDTO;
import com.example.portfolio.offering.read.dto.SkuDTO;
import com.example.portfolio.offering.repository.OfferingRepository;
import lombok.AllArgsConstructor;
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

@Service
@AllArgsConstructor
public class OfferingDAO implements DAO<UUID, OfferingDTO> {
  private final OfferingRepository offeringRepository;
  private final SKUBatchFetcher skuBatchFetcher;

  @Override
  public OfferingDTO getSingle(UUID offeringId) {
    var offering = offeringRepository
      .findById(offeringId)
      .orElseThrow();

    return convertToDTO(List.of(offering)).get(0);
  }

  @Override
  public List<OfferingDTO> get() {
    var offerings = offeringRepository
      .findAll();

    return convertToDTO(offerings);
  }

  private List<SkuDTO> buildSKUSDTO(Map<UUID, SkuDTO> skus, Offering offering) {
    return offering.getSKUs().stream().map(skus::get).collect(Collectors.toList());
  }

  private List<OfferingDTO> convertToDTO(Collection<Offering> offerings) {
    var skuPerIdFuture = asyncFetchSKUs(offerings);

    try {
      var skuPerIdMap = skuPerIdFuture.get(1000, TimeUnit.MILLISECONDS);

      return offerings.stream().map(
        offering -> OfferingDTO
          .of(
            offering,
            buildSKUSDTO(skuPerIdMap, offering)
          )
      ).collect(Collectors.toList());
    } catch (ExecutionException | InterruptedException | TimeoutException ex) {
      throw new BillingServiceUnavailableException(ex);
    }
  }

  private CompletableFuture<Map<UUID, SkuDTO>> asyncFetchSKUs(Collection<Offering> offerings) {
    var skuIds = offerings
      .stream()
      .flatMap(offering -> offering.getSKUs().stream())
      .collect(Collectors.toSet());

    return skuBatchFetcher.fetch(skuIds);
  }
}
