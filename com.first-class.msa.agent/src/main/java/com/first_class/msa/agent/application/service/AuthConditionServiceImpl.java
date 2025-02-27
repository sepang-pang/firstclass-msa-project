package com.first_class.msa.agent.application.service;

import org.springframework.stereotype.Component;

import com.first_class.msa.agent.domain.common.UserRole;
import com.first_class.msa.agent.domain.entity.DeliveryAgent;
import com.first_class.msa.agent.libs.exception.ApiException;
import com.first_class.msa.agent.libs.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthConditionServiceImpl implements AuthConditionService {

	private final HubService hubService;
	// private final MessageService messageService;

	@Override
	public void validateCreateUserRole(UserRole userRole, Long hubId, Long userId) {
		switch (userRole) {
			case MASTER -> {
				return;
			}
			case HUB_MANAGER -> {
				validateHubManager(hubId, userId);
			}
			case BUSINESS_MANAGER, DELIVERY_MANAGER ->
				throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE));
		}

	}

	@Override
	public void validateSearchUserRole(UserRole userRole, Long userId, Long hubId) {

		switch (userRole) {
			case MASTER -> {
				if (hubId != null) {
					validateExistHubId(hubId);
				}
			}
			case HUB_MANAGER -> {
				validateHubManager(userId, hubId);
			}
			case BUSINESS_MANAGER, DELIVERY_MANAGER ->
				throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE));
		}
	}

	@Override
	public void validateSearchDetailUserRole(UserRole userRole, Long userId, DeliveryAgent deliveryAgent) {

		switch (userRole) {
			case MASTER -> {
			}
			case HUB_MANAGER -> validateHubManager(userId, deliveryAgent.getHubId());
			case DELIVERY_MANAGER -> validateMatchDeliveryAgentByUserId(userId, deliveryAgent);
			case BUSINESS_MANAGER ->
				throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE));
		}
	}

	@Override
	public void validateUpdateAndDeleteUserRole(UserRole userRole, Long userId, DeliveryAgent deliveryAgent) {

		switch (userRole) {
			case MASTER -> {
			}
			case HUB_MANAGER -> validateHubManager(userId, deliveryAgent.getHubId());
			case DELIVERY_MANAGER, BUSINESS_MANAGER ->
				throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE));
		}
	}

	private void validateExistHubId(Long hubId) {
		if (!getExistHubId(hubId)) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_HUB));
		}
	}

	private void validateHubManager(Long userId, Long hubId) {
		if (!getHubIdBy(userId).equals(hubId)) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE_HUB_MANAGER));
		}
	}

	private void validateMatchDeliveryAgentByUserId(Long userId, DeliveryAgent deliveryAgent) {
		if (!userId.equals(deliveryAgent.getUserId())) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE_DELIVERY_MANAGER));
		}
	}

	private boolean getExistHubId(Long hubId) {
		return hubService.existsBy(hubId);
	}

	public Long getHubIdBy(Long userId) {
		return hubService.getHubIdBy(userId);
	}

}
