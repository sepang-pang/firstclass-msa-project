package com.first_class.msa.product.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "business-service")
public interface BusinessClient {
}
