package com.first_class.msa.orders.application.service;

import com.first_class.msa.orders.application.dto.ResDeliveryDTO;

public interface DeliveryService {
	ResDeliveryDTO getAllDeliveryBy(Long userId);
}
