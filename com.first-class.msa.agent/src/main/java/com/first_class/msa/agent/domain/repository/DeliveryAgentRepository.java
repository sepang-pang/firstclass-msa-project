package com.first_class.msa.agent.domain.repository;

import com.first_class.msa.agent.domain.entity.DeliveryAgent;

public interface DeliveryAgentRepository {

	DeliveryAgent save(DeliveryAgent deliveryAgent);
}
