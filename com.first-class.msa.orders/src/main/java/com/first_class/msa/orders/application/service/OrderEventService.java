package com.first_class.msa.orders.application.service;

import java.util.List;

import com.first_class.msa.orders.application.dto.ResOrderDTO;

public interface OrderEventService {

	void orderCreateEvent(Long orderId, Long userId, List<ResOrderDTO.OrderDTO.OrderLineDTO> orderLineDTOList);
}
