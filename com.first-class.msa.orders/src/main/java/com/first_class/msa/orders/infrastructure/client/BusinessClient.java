package com.first_class.msa.orders.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.first_class.msa.orders.application.service.BusinessService;
@FeignClient(name = "business-service")
public interface BusinessClient extends BusinessService {
}
