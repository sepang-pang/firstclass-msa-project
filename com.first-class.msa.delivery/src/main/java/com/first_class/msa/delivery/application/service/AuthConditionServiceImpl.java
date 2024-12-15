package com.first_class.msa.delivery.application.service;

import org.springframework.stereotype.Component;

import com.first_class.msa.delivery.application.dto.ResDeliveryAgentGetByUserIdDTO;
import com.first_class.msa.delivery.domain.common.UserRole;
import com.first_class.msa.delivery.domain.model.Delivery;
import com.first_class.msa.delivery.libs.exception.ApiException;
import com.first_class.msa.delivery.libs.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthConditionServiceImpl implements AuthConditionService {

	private final HubService hubService;
	private final AgentService agentService;

	@Override
	public void HubStatusPutByAuthCondition(
		UserRole userRole,
		Long userId,
		Delivery delivery
	) {
		switch (userRole) {
			case MASTER -> {
				return;
			}
			case HUB_MANAGER -> {
				Long hubId = getHubIdBy(userId);
				delivery.getHubDeliveryRouteList().stream()
					.filter(route -> route.getArrivalHubId().equals(hubId))
					.findFirst()
					.orElseThrow(
						() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_HUB_DELIVERY_ROUTE))
					);
			}
			case DELIVERY_MANAGER -> {
				Long deliveryAgentId = getDeliveryAgentId(userId);
					agentService.getDeliveryAgentByUserId(userId);
				delivery.getHubDeliveryRouteList().stream()
					.filter(route -> route.getHubAgentId().equals(deliveryAgentId))
					.findFirst()
					.orElseThrow(
						() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_HUB_DELIVERY_ROUTE))
					);
			}
			case BUSINESS_MANAGER ->
				throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE));
		}
	}

	private boolean getExistHubId(Long hubId) {
		return hubService.existsBy(hubId);
	}

	public Long getHubIdBy(Long userId) {
		return hubService.getHubIdBy(userId);
	}
	private Long getDeliveryAgentId(Long userId) {
		ResDeliveryAgentGetByUserIdDTO resDeliveryAgentGetByUserIdDTO = agentService.getDeliveryAgentByUserId(userId);
		return resDeliveryAgentGetByUserIdDTO.getDeliveryAgentId();
	}
}
