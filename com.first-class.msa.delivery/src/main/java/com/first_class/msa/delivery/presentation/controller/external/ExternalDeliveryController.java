package com.first_class.msa.delivery.presentation.controller.external;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.first_class.msa.delivery.application.dto.ResDeliveryOrderSearchDTO;
import com.first_class.msa.delivery.application.service.DeliveryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExternalDeliveryController {
	private final DeliveryService deliveryService;

	@GetMapping("/external/orders/deliveries")
	ResDeliveryOrderSearchDTO getAllDeliveryBy(@RequestParam List<Long> orderIdList){
		return deliveryService.getAllDeliveryBy(orderIdList);
	}

	@GetMapping("/external/orders/{orderId}/deliveries")
	boolean existDeliveryBy(@PathVariable Long orderId, @RequestParam Long userId){
		return deliveryService.existDeliveryBy(orderId, userId);
	}
}
