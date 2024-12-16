package com.first_class.msa.delivery.application.service;

import com.first_class.msa.delivery.domain.common.UserRole;
import com.first_class.msa.delivery.domain.model.Delivery;

public interface AuthConditionService {

	void validateHubStatusPutByAuthCondition(UserRole userRole, Long userId, Delivery delivery);

	void validateBusinessStatusPutByAuthCondition(UserRole userRole, Long userId, Delivery delivery);

	void validateSearchByAuthCondition(UserRole userRole, Long userId, Delivery delivery);

	void validateDeleteByAuthCondition(UserRole userRole, Long userId, Delivery delivery);




}
