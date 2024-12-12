package com.first_class.msa.agent.infratructure.repository;

import org.springframework.stereotype.Repository;

import com.first_class.msa.agent.domain.repository.DeliveryAgentRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryAgentRepositoryImpl implements DeliveryAgentRepository {
	private final DeliveryAgentJpaRepository deliveryAgentJpaRepository;


}
