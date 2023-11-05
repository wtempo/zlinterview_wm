package com.example.portfolio.offering.repository;

import com.example.portfolio.offering.model.domain.CustomerOffering;
import com.example.portfolio.offering.model.domain.Subscription;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerOfferingRepository extends JpaRepository<CustomerOffering, UUID>, JpaSpecificationExecutor<Subscription> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "jakarta.persistence.query.timeout", value = "1000")}) // lock.timeout would be better, but does not work  with Postgres
  @Query(value = "SELECT co FROM CustomerOffering co WHERE co.customer.id = :customerId AND co.offering.id = :serviceId")
  Optional<CustomerOffering> findByCustomerIdAndServiceIdWithLock(@Param("customerId") UUID customerId, @Param("serviceId") UUID serviceId);
}
