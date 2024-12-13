package com.first_class.msa.agent.application.service;

import com.first_class.msa.agent.domain.common.UserRole;
import com.first_class.msa.agent.domain.entity.DeliveryAgent;

public interface AuthConditionService {

	void validateCreateUserRole(UserRole userRole, Long hubId, Long userId);

	void validateSearchUserRole(UserRole userRole, Long userId, Long hubId);

	void validateSearchDetailUserRole(UserRole userRole, Long userId, DeliveryAgent deliveryAgent);


}
