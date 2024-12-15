package com.first_class.msa.delivery.application.service;

import java.util.List;

import com.first_class.msa.delivery.domain.model.Delivery;
import com.first_class.msa.delivery.domain.model.HubDeliveryRoute;

public interface HubDeliveryService {
	List<HubDeliveryRoute> CreateHubDeliveryRoute(Delivery delivery);
}
