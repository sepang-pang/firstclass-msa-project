package com.first_class.msa.orders.application.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.first_class.msa.orders.application.dto.AuthSearchConditionDTO;
import com.first_class.msa.orders.application.dto.ResBusinessDTO;
import com.first_class.msa.orders.application.dto.ResDeliveryOrderSearchDTO;
import com.first_class.msa.orders.application.dto.ResDeliveryOrderSearchDetailDTO;
import com.first_class.msa.orders.application.dto.ResHubDTO;
import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.model.UserRole;
import com.first_class.msa.orders.libs.exception.ApiException;
import com.first_class.msa.orders.libs.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthConditionServiceImpl implements AuthConditionService{
	private final HubService hubService;
	private final BusinessService businessService;
	private final DeliveryService deliveryService;

	@Override
	public AuthSearchConditionDTO createSearchCondition(UserRole userRole, Long userId) {
		return switch (userRole) {
			case MASTER -> AuthSearchConditionDTO.createForMaster();
			case HUB_MANAGER -> {
				Long hubId = getHubId(userId);
				yield AuthSearchConditionDTO.createForHubManager(hubId);
			}
			case BUSINESS_MANAGER -> {
				Long businessId = getBusinessId(userId);
				yield AuthSearchConditionDTO.createForBusinessManager(businessId);
			}
			case DELIVERY_MANAGER -> {
				List<Long> deliveryOrders = getDeliveryOrders(userId);
				yield AuthSearchConditionDTO.createForDeliveryManager(deliveryOrders);
			}
		};
	}

	@Override
	public void validateOrderDetailAuth(UserRole userRole, Long userId, Order order) {
		switch (userRole) {
			case HUB_MANAGER -> validateHubManager(userId, order);
			case BUSINESS_MANAGER -> validateBusinessManager(userId, order);
			case DELIVERY_MANAGER -> validateDeliveryManager(userId, order);
			default -> throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE));
		}
	}

	@Override
	public void validateDeleteAuth(UserRole userRole, Long userId, Order order) {
		if (UserRole.MASTER.equals(userRole)) {
			return;
		}

		if (UserRole.HUB_MANAGER.equals(userRole)) {
			validateHubManager(userId, order);
		} else {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE));
		}
	}

	private void validateHubManager(Long userId, Order order) {
		Long hubId = getHubId(userId);
		if (!order.getHubId().equals(hubId)) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE_HUB_MANAGER));
		}
	}

	private void validateBusinessManager(Long userId, Order order) {
		Long businessId = getBusinessId(userId);
		if (!order.getBusinessId().equals(businessId)) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE_BUSINESS_MANAGER));
		}
	}

	private void validateDeliveryManager(Long userId, Order order) {
		ResDeliveryOrderSearchDetailDTO deliveryDetail = deliveryService.getDeliveryBy(userId, order.getId());
		if (!deliveryDetail.getOrderId().equals(order.getId())) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE_DELIVERY_MANAGER));
		}
	}

	private Long getHubId(Long userId) {
		ResHubDTO hubDTO = hubService.getHubBy(userId);
		return hubDTO.getHubId();
	}

	private Long getBusinessId(Long userId) {
		ResBusinessDTO businessDTO = businessService.getBusinessUserBy(userId);
		return businessDTO.getBusinessId();
	}

	private List<Long> getDeliveryOrders(Long userId) {
		ResDeliveryOrderSearchDTO deliveryOrders = deliveryService.getAllDeliveryBy(userId);
		return deliveryOrders.getOrderIdList();
	}
}
