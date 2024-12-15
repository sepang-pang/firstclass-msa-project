package com.first_class.msa.delivery.application.service;

import com.first_class.msa.delivery.domain.common.HubStatus;
import com.first_class.msa.delivery.infrastructure.event.OrderCreateDeliveryEvent;

public interface DeliveryService {

	void handleOrderCreateDeliveryEvent(OrderCreateDeliveryEvent event);

	void HubStatusRoutePutBy(Long userId, Long deliveryId, Long hubDeliveryRouteId, HubStatus hubStatus);

}
