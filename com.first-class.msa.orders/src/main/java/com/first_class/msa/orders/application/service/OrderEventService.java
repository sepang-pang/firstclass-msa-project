package com.first_class.msa.orders.application.service;

import java.util.List;

import com.first_class.msa.orders.application.dto.ResOrderDTO;

public interface OrderEventService {

	void orderCreateProductEvent(Long orderId, List<ResOrderDTO.OrderDTO.OrderLineDTO> orderLineDTOList);

	void orderCreateDeliveryEvent(Long orderId, String address);

	void orderDeleteProductEvent(Long orderId, List<ResOrderDTO.OrderDTO.OrderLineDTO> orderLineDTOList);

	void orderDeleteDeliveryEvent(Long orderId, Long userId);
}
