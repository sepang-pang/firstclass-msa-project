package com.first_class.msa.orders.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.first_class.msa.orders.application.service.ProductService;

@FeignClient(name = "product-service")
public interface ProductClient extends ProductService {
}
