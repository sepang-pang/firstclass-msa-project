package com.first_class.msa.agent.application.service;

import com.first_class.msa.agent.domain.common.UserRole;

public interface AuthConditionService {

	void validateUserRole(UserRole userRole, Long hubId, Long userId);
	void validateHubManager(Long hubId , Long userId);

}
