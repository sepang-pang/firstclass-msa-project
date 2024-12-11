package com.first_class.msa.orders.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.first_class.msa.orders.application.dto.ResOrderPostDTO;
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
		List<ResOrderPostDTO.OrderDTO.OrderLineDTO> orderLineDTOList)
	{
		OrderCreateEvent orderCreateEvent = new OrderCreateEvent(orderId, userId, orderLineDTOList);
		orderEventPublisher.publishEvent(RabbitMQConfig.ORDER_CREATED_KEY ,orderCreateEvent);
	}
}
