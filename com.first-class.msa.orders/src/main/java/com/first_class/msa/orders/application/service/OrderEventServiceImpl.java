package com.first_class.msa.orders.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.first_class.msa.orders.application.dto.ResOrderDTO;
import com.first_class.msa.orders.infrastructure.config.RabbitMQConfig;
import com.first_class.msa.orders.infrastructure.event.OrderCreateEvent;
import com.first_class.msa.orders.infrastructure.messaging.OrderEventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderEventServiceImpl implements OrderEventService{

	private final OrderEventPublisher orderEventPublisher;

	@Override
	public void orderCreateEvent(
		Long orderId,
		Long userId,
		List<ResOrderDTO.OrderDTO.OrderLineDTO> orderLineDTOList)
	{
		OrderCreateEvent orderCreateEvent = new OrderCreateEvent(orderId, userId, orderLineDTOList);
		orderEventPublisher.publishEvent(RabbitMQConfig.ORDER_CREATED_KEY ,orderCreateEvent);
	@Override
	public void orderDeleteProductEvent(
		Long orderId,
		List<ResOrderDTO.OrderDTO.OrderLineDTO> orderLineDTOList) {

		OrderDeleteProductEvent orderDeleteProductEvent = new OrderDeleteProductEvent(orderId, orderLineDTOList);
		orderEventPublisher.publishEvent(RabbitMQConfig.ORDER_DELETED_PRODUCT_KEY , orderDeleteProductEvent);
	}

	@Override
	public void orderDeleteDeliveryEvent(Long orderId, Long userId) {
		OrderDeleteDeliveryEvent orderDeleteDeliveryEvent = new OrderDeleteDeliveryEvent(orderId, userId);
		orderEventPublisher.publishEvent(RabbitMQConfig.ORDER_DELETED_DELIVERY_KEY, orderDeleteDeliveryEvent);
	}
}
