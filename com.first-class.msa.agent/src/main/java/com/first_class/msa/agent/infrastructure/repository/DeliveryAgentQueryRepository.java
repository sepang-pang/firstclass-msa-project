package com.first_class.msa.agent.infrastructure.repository;

import org.springframework.data.domain.Pageable;

import com.first_class.msa.agent.application.dto.DeliveryAgentAuthSearchConditionDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentSearchDTO;

public interface DeliveryAgentQueryRepository {

	ResDeliveryAgentSearchDTO findByAllDeliveryAgent(
		DeliveryAgentAuthSearchConditionDTO deliveryAgentAuthSearchConditionDTO,
		Pageable pageable
	);
}
