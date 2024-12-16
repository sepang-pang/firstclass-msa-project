package com.first_class.msa.orders.application.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.orders.application.dto.AuthSearchConditionDTO;
import com.first_class.msa.orders.application.dto.ResBusinessDTO;
import com.first_class.msa.orders.application.dto.ResOrderDTO;
import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;
import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.model.OrderLine;
import com.first_class.msa.orders.domain.model.UserRole;
import com.first_class.msa.orders.domain.model.valueobject.Address;
import com.first_class.msa.orders.domain.model.valueobject.RequestInfo;
import com.first_class.msa.orders.domain.repository.OrderRepository;
import com.first_class.msa.orders.infrastructure.config.RabbitMQConfig;
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
	private final AuthService authService;
	private final BusinessService businessService;
	private final AuthConditionService authConditionService; // 추가된 의존성

	@Override
	@Transactional
	public ResOrderDTO postBy(Long businessId, Long userId, ReqOrderPostDTO reqOrderPostDTO) {
		Address address = new Address(reqOrderPostDTO.getAddress());
		RequestInfo requestInfo = new RequestInfo(reqOrderPostDTO.getRequestInfo());
		ResBusinessDTO resBusinessDTO = businessService.getBusinessBy(businessId);
		ResBusinessDTO resDeliveryBusinessDTO = businessService.getBusinessBy(reqOrderPostDTO.getArrivalBusinessId());


		Order order = Order.createOrder(
			resBusinessDTO.getBusinessId(),
			resDeliveryBusinessDTO.getBusinessId(),
			resBusinessDTO.getHubId(),
			resDeliveryBusinessDTO.getHubId(),
			userId,
			address,
			requestInfo);
		order.setCreateByAndUpdateBy(userId);

		List<OrderLine> orderLineList = orderLineService.createOrderLineList(
			reqOrderPostDTO.getReqOrderLinePostDTOList(), order);

		order.addOrderLineList(orderLineList);
		order.updateOrderTotalPrice(orderLineList);
		order = orderRepository.save(order);

		ResOrderDTO orderPostDTO = ResOrderDTO.of(order);
		orderEventService.orderCreateProductEvent(order);
		orderEventService.orderCreateDeliveryEvent(order);

		return orderPostDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public ResOrderSearchDTO getAllOrderBy(Long userId, Pageable pageable) {
		String userRole = authService.getRoleBy(userId).getRole();
		AuthSearchConditionDTO searchCondition
			= authConditionService.createSearchCondition(UserRole.valueOf(userRole), userId);
		return orderRepository.findAll(searchCondition, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public ResOrderDTO getOrderDetailBy(Long userId, Long orderId) {
		String userRole = authService.getRoleBy(userId).getRole();
		Order order = findById(orderId);

		authConditionService.validateOrderDetailAuth(UserRole.valueOf(userRole), userId, order);

		return ResOrderDTO.of(order);
	}

	@Override
	@Transactional
	public void deleteBy(Long userId, Long orderId) {
		String userRole = authService.getRoleBy(userId).getRole();
		Order order = findById(orderId);

		authConditionService.validateDeleteAuth(UserRole.valueOf(userRole), userId, order);

		order.deleteOrder(userId);

		orderEventService.orderDeleteProductEvent(
			order.getId(),
			ResOrderDTO.OrderDTO.OrderLineDTO.from(order.getOrderLineList())
		);
		orderEventService.orderDeleteDeliveryEvent(order.getId(), userId);
	}

	@Transactional
	@RabbitListener(queues = RabbitMQConfig.ORDER_FAILED_KEY)
	public void handleFailedOrder(Long orderId) {
		Order order = findById(orderId);
		order.deleteOrder(order.getUserId());
		orderEventService.orderCancelEvent(order.getUserId(), order.getId());
	}

	private Order findById(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_ORDER)));
	}




}
