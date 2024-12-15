package com.first_class.msa.delivery.application.service;

import com.first_class.msa.delivery.domain.model.BusinessDeliveryRoute;

public interface BusinessDeliveryService {

	BusinessDeliveryRoute createBusinessDeliveryRoute(Long arrivalHubId, Long businessId, String address);

	void assignAgentToBusinessDeliveryRoute(BusinessDeliveryRoute businessDeliveryRoute);
}
