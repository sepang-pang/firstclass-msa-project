package com.first_class.msa.delivery.application.service;

import com.first_class.msa.delivery.infrastructure.event.OrderCreateDeliveryEvent;

public interface DeliveryService {

	void handleOrderCreateDeliveryEvent(OrderCreateDeliveryEvent event);
}
