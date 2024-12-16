package com.first_class.msa.agent.infrastructure.repository;

import java.util.List;
import java.util.Optional;

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

	@Override
	public Optional<DeliveryAgent> findById(Long deliveryAgentId) {
		return deliveryAgentJpaRepository.findByIdAndDeletedByIsNull(deliveryAgentId);
	}

	@Override
	public Integer findMaxSequenceByHubId(Long hubId){
		return deliveryAgentJpaRepository.findMaxSequenceByHubId(hubId);
	}

	@Override
	public Integer findMaxSequenceByHubIdIsNull() {
		return deliveryAgentJpaRepository.findMaxSequenceByHubIdIsNull();
	}

	@Override
	public List<DeliveryAgent> findByHubIdIsNull() {
		return deliveryAgentJpaRepository.findByHubIdIsNull();
	}

	@Override
	public List<DeliveryAgent> findByHubId(Long hubId) {
		return deliveryAgentJpaRepository.findByHubId(hubId);
	}

	@Override
	public Optional<DeliveryAgent> findByUserId(Long userId) {
		return deliveryAgentJpaRepository.findByUserIdAndDeletedByIsNull(userId);
	}

}
