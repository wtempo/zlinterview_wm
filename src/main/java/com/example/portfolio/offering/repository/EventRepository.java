package com.example.portfolio.offering.repository;

import com.example.portfolio.offering.model.infra.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<OutboxEvent, UUID> {}
