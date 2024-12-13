package com.first_class.msa.agent.application.service;

import org.springframework.stereotype.Component;

import com.first_class.msa.agent.domain.common.UserRole;
import com.first_class.msa.agent.libs.exception.ApiException;
import com.first_class.msa.agent.libs.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthConditionServiceImpl implements AuthConditionService {

	private final HubService hubService;
	// private final MessageService messageService;

	@Override
	public void validateUserRole(UserRole userRole, Long hubId, Long userId) {
		switch (userRole) {
			case MASTER -> {
				return;
			}
			case HUB_MANAGER -> validateHubManager(hubId, userId);
			case BUSINESS_MANAGER, DELIVERY_MANAGER ->
				throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE));
		}

	}

	@Override
	public void validateHubManager(Long userId, Long hubId) {
		if (!getExistHubId(hubId)) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE_HUB_MANAGER));
		}
		if (!getHubIdBy(userId).equals(hubId)) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE_HUB_MANAGER));
		}
	}

	private boolean getExistHubId(Long hubId) {
		return hubService.existsBy(hubId);
	}

	private Long getHubIdBy(Long userId) {
		return hubService.getHubIdBy(userId);
	}

}
