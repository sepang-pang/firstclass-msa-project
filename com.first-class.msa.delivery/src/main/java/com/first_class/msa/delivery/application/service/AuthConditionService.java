package com.first_class.msa.delivery.application.service;

import com.first_class.msa.delivery.domain.common.UserRole;
import com.first_class.msa.delivery.domain.model.Delivery;

public interface AuthConditionService {

	void hubStatusPutByAuthCondition(UserRole userRole, Long userId, Delivery delivery);

	void businessStatusPutByAuthCondition(UserRole userRole, Long userId, Delivery delivery);




}
