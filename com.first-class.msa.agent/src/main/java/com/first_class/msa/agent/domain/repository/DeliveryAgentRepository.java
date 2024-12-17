package com.first_class.msa.agent.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.first_class.msa.agent.application.dto.DeliveryAgentAuthSearchConditionDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentSearchDTO;
import com.first_class.msa.agent.domain.entity.DeliveryAgent;

public interface DeliveryAgentRepository {

	DeliveryAgent save(DeliveryAgent deliveryAgent);


	ResDeliveryAgentSearchDTO findByAllDeliveryAgent(
		DeliveryAgentAuthSearchConditionDTO deliveryAgentAuthSearchConditionDTO,
		Pageable pageable
	);

	Optional<DeliveryAgent> findById(Long deliveryAgentId);

	public Integer findMaxSequenceByHubIdAndTypeHub(Long hubId);

	public Integer findMaxSequenceByHubIdAndTypeDelivery(Long hubId);

	DeliveryAgent findByDeliveryIdAndHubId(Long hubId);
	List<DeliveryAgent> findByHubId(Long hubId);

	Optional<DeliveryAgent> findByUserId(Long userId);


}
