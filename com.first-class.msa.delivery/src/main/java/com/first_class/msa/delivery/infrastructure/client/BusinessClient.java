package com.first_class.msa.delivery.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.first_class.msa.delivery.application.dto.ExternalResBusinessGetByIdDTO;
import com.first_class.msa.delivery.application.service.BusinessService;

@FeignClient("business-service")
public interface BusinessClient extends BusinessService {
	@GetMapping("/external/business/{businessId}")
	ExternalResBusinessGetByIdDTO getBy(@PathVariable("businessId") Long businessId);
}
