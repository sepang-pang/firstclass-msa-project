package com.first_class.msa.agent.application.service;

import com.first_class.msa.agent.domain.common.UserRole;

public interface AuthConditionService {

	void validateCreateUserRole(UserRole userRole, Long hubId, Long userId);

	void validateSearchUserRole(UserRole userRole, Long userId, Long hubId);

	void validateExistHubId(Long hubId);

}
