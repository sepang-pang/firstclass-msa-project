package com.first_class.msa.orders.application.service;

import java.util.List;

import com.first_class.msa.orders.application.dto.ResOrderDTO;
import com.first_class.msa.orders.domain.model.Order;

public interface OrderEventService {

	void orderCreateProductEvent(Order order);

	void orderCreateDeliveryEvent(Order order);

	void orderDeleteProductEvent(Long orderId, List<ResOrderDTO.OrderDTO.OrderLineDTO> orderLineDTOList);

	void orderDeleteDeliveryEvent(Long orderId, Long userId);

	void orderCancelEvent(Long userId, Long orderId);
}
