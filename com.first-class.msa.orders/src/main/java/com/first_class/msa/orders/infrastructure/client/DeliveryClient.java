package com.first_class.msa.orders.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.first_class.msa.orders.application.dto.ResDeliveryOrderSearchDTO;
import com.first_class.msa.orders.application.service.DeliveryService;

@FeignClient(name = "delivery-service")
public interface DeliveryClient extends DeliveryService {

	@GetMapping("/external/orders/deliveries")
	ResDeliveryOrderSearchDTO getAllDeliveryBy(@RequestParam Long userId);

	@GetMapping("/external/orders/{orderId}/deliveries")
	boolean existDeliveryBy(@PathVariable Long orderId, @RequestParam Long userId);



}
