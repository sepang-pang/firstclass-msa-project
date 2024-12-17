package com.first_class.msa.orders.application.service;

import com.first_class.msa.orders.application.dto.ResDeliveryOrderSearchDTO;

public interface DeliveryService {
	ResDeliveryOrderSearchDTO getAllDeliveryBy(Long userId);

	boolean existDeliveryBy(Long orderId , Long userId);
}
