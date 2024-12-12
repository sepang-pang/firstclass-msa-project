package com.first_class.msa.orders.application.service;

import com.first_class.msa.orders.application.dto.AuthSearchConditionDTO;
import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.model.UserRole;

public interface AuthConditionService {

	AuthSearchConditionDTO createSearchCondition(UserRole userRole, Long userId);

	void validateOrderDetailAuth(UserRole userRole, Long userId, Order order);

	void validateDeleteAuth(UserRole userRole, Long userId, Order order);
}
