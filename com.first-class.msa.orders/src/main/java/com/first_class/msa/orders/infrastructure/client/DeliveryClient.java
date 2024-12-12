package com.first_class.msa.orders.infrastructure.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.first_class.msa.orders.application.dto.ResDeliveryDTO;
import com.first_class.msa.orders.application.service.DeliveryService;

@FeignClient(name = "delivery-service")
public interface DeliveryClient extends DeliveryService {

	@GetMapping("/orders/deliveries")
	ResDeliveryDTO getAllDeliveryBy(@RequestParam List<Long> orderIdList);

}
