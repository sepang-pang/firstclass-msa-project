package com.first_class.msa.agent.infratructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.first_class.msa.agent.domain.entity.DeliveryAgent;

public interface DeliveryAgentJpaRepository extends JpaRepository<DeliveryAgent, Long> {
}
