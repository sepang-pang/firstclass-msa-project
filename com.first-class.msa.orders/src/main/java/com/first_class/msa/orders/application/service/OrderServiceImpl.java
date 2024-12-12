package com.first_class.msa.orders.application.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.orders.application.dto.AuthSearchConditionDTO;
import com.first_class.msa.orders.application.dto.ResBusinessDTO;
import com.first_class.msa.orders.application.dto.ResDeliveryOrderSearchDTO;
import com.first_class.msa.orders.application.dto.ResDeliveryOrderSearchDetailDTO;
import com.first_class.msa.orders.application.dto.ResHubDTO;
import com.first_class.msa.orders.application.dto.ResOrderDTO;
import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;
import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.model.OrderLine;
import com.first_class.msa.orders.domain.model.valueobject.RequestInfo;
import com.first_class.msa.orders.domain.repository.OrderRepository;
import com.first_class.msa.orders.libs.exception.ApiException;
import com.first_class.msa.orders.libs.message.ErrorMessage;
import com.first_class.msa.orders.presentation.request.ReqOrderPostDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final OrderLineService orderLineService;
	private final OrderEventService orderEventService;
	private final HubService hubService;
	private final BusinessService businessService;
	private final AuthService authService;
	private final DeliveryService deliveryService;

	@Override
	@Transactional
	public ResOrderDTO postBy(Long businessId, Long userId, ReqOrderPostDTO reqOrderPostDTO) {

		RequestInfo requestInfo = new RequestInfo(reqOrderPostDTO.getRequestInfo());
		ResBusinessDTO resbusinessDTO = businessService.getBusinessBy(businessId);
		Order order = Order.createOrder(businessId, resbusinessDTO.getHubId(), userId, requestInfo);
		order.setCreateByAndUpdateBy(userId);

		List<OrderLine> orderLineList
			= orderLineService.createOrderLineList(reqOrderPostDTO.getReqOrderLinePostDTOList(), order);

		order.addOrderLineList(orderLineList);
		order.updateOrderTotalPrice(orderLineList);
		order = orderRepository.save(order);

		ResOrderDTO orderPostDTO = ResOrderDTO.of(order);
		orderEventService.orderCreateEvent(order.getId(), userId, orderPostDTO.getOrderDTO().getOrderLineList());

		return orderPostDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public ResOrderSearchDTO getAllOrderBy(Long userId, Pageable pageable) {
		String userRole = authService.getRoleBy(userId).getRole();
		return orderRepository.findAll(SearchCondition(userId, userRole), pageable);
	}

	private AuthSearchConditionDTO SearchCondition(Long userId, String userRole) {

		return switch (userRole) {
			case "MASTER" -> AuthSearchConditionDTO.createForMaster();
			case "HUB_MANAGER" -> {
				ResHubDTO resHubDto = hubService.getHubBy(userId);
				yield AuthSearchConditionDTO.createForHubManager(resHubDto.getHubId());
			}
			case "BUSINESS_MANAGER" -> {
				ResBusinessDTO businessDTO = businessService.getBusinessUserBy(userId);
				yield AuthSearchConditionDTO.createForBusinessManager(businessDTO.getBusinessId());
			}
			case "DELIVERY_MANAGER" -> {
				ResDeliveryOrderSearchDTO resDeliveryOrderSearchDTO = deliveryService.getAllDeliveryBy(userId);
				yield AuthSearchConditionDTO.createForDeliveryManager(
					resDeliveryOrderSearchDTO.getOrderIdList());
			}
			default -> throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE));
		};
	}

	@Transactional(readOnly = true)
	public ResOrderDTO getOrderDetailBy(Long userId, Long orderId) {
		String userRole = authService.getRoleBy(userId).getRole();
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_ORDER)));
		AuthCondition(userId, userRole, order);

		return ResOrderDTO.of(order);
	}

	private void AuthCondition(Long userId, String userRole, Order order) {
		switch (userRole) {
			case "HUB_MANAGER" -> {
				ResHubDTO resHubDto = hubService.getHubBy(userId);
				if (!order.getHubId().equals(resHubDto.getHubId())) {
					throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE_HUB_MANAGER));
				}
			}
			case "BUSINESS_MANAGER" -> {
				ResBusinessDTO businessDTO = businessService.getBusinessUserBy(userId);
				if (!order.getBusinessId().equals(businessDTO.getBusinessId())) {
					throw new IllegalArgumentException(
						new ApiException(ErrorMessage.INVALID_USER_ROLE_BUSINESS_MANAGER));
				}
			}
			case "DELIVERY_MANAGER" -> {
				ResDeliveryOrderSearchDetailDTO resDeliveryOrderSearchDetailDTO
					= deliveryService.getDeliveryBy(userId, order.getId());
				if (!(resDeliveryOrderSearchDetailDTO.getOrderId().equals(order.getId()) &&
					resDeliveryOrderSearchDetailDTO.getDeliveryUserId().equals(userId))
				)
					throw new IllegalArgumentException(
						new ApiException(ErrorMessage.INVALID_USER_ROLE_DELIVERY_MANAGER));
			}
		}

	@Override
	@Transactional
	public void deleteBy(Long userId, Long orderId){
		Order order = findById(orderId);
		String userRole = authService.getRoleBy(userId).getRole();
		deleteByAuthCondition(userId, userRole, order);
		order.deleteOrder(userId);
		
		orderEventService.orderDeleteProductEvent(
			order.getId(), 
			ResOrderDTO.OrderDTO.OrderLineDTO.from(order.getOrderLineList())
		);
		orderEventService.orderDeleteDeliveryEvent(order.getId(), userId);
		
	}

	private void deleteByAuthCondition(Long userId, String userRole, Order order) {
		if(!(userRole.equals("MASTER") || userRole.equals("HUB_MANAGER"))){
			throw new IllegalArgumentException(new ApiException(ErrorMessage.INVALID_USER_ROLE));
		} else if(userRole.equals("HUB_MANAGER")) {
			ResHubDTO resHubDto = hubService.getHubBy(userId);
			if (!order.getHubId().equals(resHubDto.getHubId())) {
				throw new IllegalArgumentException(
					new ApiException(ErrorMessage.INVALID_USER_ROLE_HUB_MANAGER
					));
			}
		}

	}


	private Order findById(Long orderId){
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_ORDER)));
	}

	// TODO: 2024-12-12 배송 주소 변경관련 메세지큐 처리 


}
