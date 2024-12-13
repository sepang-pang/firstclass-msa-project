package com.first_class.msa.agent.infratructure.repository;

import org.springframework.stereotype.Repository;

import com.first_class.msa.agent.domain.entity.DeliveryAgent;
import com.first_class.msa.agent.domain.repository.DeliveryAgentRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryAgentRepositoryImpl implements DeliveryAgentRepository {
	private final DeliveryAgentJpaRepository deliveryAgentJpaRepository;

	@Override
	public DeliveryAgent save(DeliveryAgent deliveryAgent) {
		return deliveryAgentJpaRepository.save(deliveryAgent);
	}
}
