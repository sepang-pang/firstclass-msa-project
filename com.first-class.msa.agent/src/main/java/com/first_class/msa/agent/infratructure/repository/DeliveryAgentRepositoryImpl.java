package com.first_class.msa.agent.infratructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.first_class.msa.agent.application.dto.DeliveryAgentAuthSearchConditionDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentSearchDTO;
import com.first_class.msa.agent.domain.entity.DeliveryAgent;
import com.first_class.msa.agent.domain.repository.DeliveryAgentRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryAgentRepositoryImpl implements DeliveryAgentRepository {
	private final DeliveryAgentJpaRepository deliveryAgentJpaRepository;
	private final DeliveryAgentQueryRepository deliveryAgentQueryRepository;

	@Override
	public DeliveryAgent save(DeliveryAgent deliveryAgent) {
		return deliveryAgentJpaRepository.save(deliveryAgent);
	}

	@Override
	public ResDeliveryAgentSearchDTO findByAllDeliveryAgent(
		DeliveryAgentAuthSearchConditionDTO deliveryAgentAuthSearchConditionDTO,
		Pageable pageable
	){
		return deliveryAgentQueryRepository.findByAllDeliveryAgent(deliveryAgentAuthSearchConditionDTO, pageable);
	}
}
