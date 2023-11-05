package com.example.portfolio.offering.repository;

import com.example.portfolio.offering.model.domain.Offering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OfferingRepository extends JpaRepository<Offering, UUID>, JpaSpecificationExecutor<Offering> {}
