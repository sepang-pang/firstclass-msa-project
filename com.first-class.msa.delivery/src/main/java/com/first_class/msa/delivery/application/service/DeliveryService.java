package com.first_class.msa.delivery.application.service;

import java.util.List;

import com.first_class.msa.delivery.application.dto.ResDeliveryOrderSearchDTO;
import com.first_class.msa.delivery.application.dto.ResDeliverySearchDTO;
import com.first_class.msa.delivery.infrastructure.event.OrderCreateDeliveryEvent;
import com.first_class.msa.delivery.presentation.dto.ReqBusinessDeliveryPutDTO;
import com.first_class.msa.delivery.presentation.dto.ReqHubDeliveryPutDTO;

public interface DeliveryService {

	void handleOrderCreateDeliveryEvent(OrderCreateDeliveryEvent event);

	void hubRouteStatusPutBy(
		Long userId, Long deliveryId,
		Long hubDeliveryRouteId,
		ReqHubDeliveryPutDTO reqHubDeliveryPutDTO
	);

	void businessDeliveryStatusPutBy(
		Long userId,
		Long deliveryId,
		Long businessDeliveryRouteId,
		ReqBusinessDeliveryPutDTO reqBusinessDeliveryPutDTO
	);

	ResDeliverySearchDTO getSearchById(Long userId, Long deliveryId);

	void deleteDeliveryById(Long userId, Long deliveryId);

	ResDeliveryOrderSearchDTO getAllDeliveryBy(List<Long> orderIdList);
	boolean existDeliveryBy(Long orderId, Long userId);
}
