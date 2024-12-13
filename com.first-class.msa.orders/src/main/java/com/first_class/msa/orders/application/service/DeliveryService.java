package com.first_class.msa.orders.application.service;

import com.first_class.msa.orders.application.dto.ResDeliveryOrderSearchDTO;
import com.first_class.msa.orders.application.dto.ResDeliveryOrderSearchDetailDTO;

public interface DeliveryService {
	ResDeliveryOrderSearchDTO getAllDeliveryBy(Long userId);

	ResDeliveryOrderSearchDetailDTO getDeliveryBy(Long orderId , Long userId);
}
