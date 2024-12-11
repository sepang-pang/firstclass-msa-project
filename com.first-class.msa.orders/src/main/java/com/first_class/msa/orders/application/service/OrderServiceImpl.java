package com.first_class.msa.orders.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.orders.application.dto.ResOrderPostDTO;
import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.model.OrderLine;
import com.first_class.msa.orders.domain.model.valueobject.RequestInfo;
import com.first_class.msa.orders.domain.repository.OrderRepository;
import com.first_class.msa.orders.presentation.request.ReqOrderPostDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
	private final OrderRepository orderRepository;
	private final OrderLineService orderLineService;
	private final OrderEventService orderEventService;
	@Override
	@Transactional
	public ResOrderPostDTO postBy(Long businessId, Long userId, ReqOrderPostDTO reqOrderPostDTO){

		RequestInfo requestInfo = new RequestInfo(reqOrderPostDTO.getRequestInfo());
		Order order = Order.createOrder(businessId, userId,requestInfo);
		List<OrderLine> orderLineList
			= orderLineService.createOrderLineList(reqOrderPostDTO.getReqOrderLinePostDTOList(), order);

		order.addOrderLineList(orderLineList);
		order.updateOrderTotalPrice(orderLineList);
		order = orderRepository.save(order);

		ResOrderPostDTO orderPostDTO = ResOrderPostDTO.of(order);
		orderEventService.orderCreateEvent(order.getId(), userId, orderPostDTO.getOrderDTO().getOrderLineList());


		return orderPostDTO;
	}
}
