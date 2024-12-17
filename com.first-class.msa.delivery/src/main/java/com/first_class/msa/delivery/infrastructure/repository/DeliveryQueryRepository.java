package com.first_class.msa.delivery.infrastructure.repository;

import java.util.List;

public interface DeliveryQueryRepository {

	List<Long> findOrderIdsByAgentId(Long agentId);

}
