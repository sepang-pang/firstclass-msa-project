package com.first_class.msa.orders.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.first_class.msa.orders.application.service.AuthService;
@FeignClient(name = "business-service")
public interface AuthClient extends AuthService {
}
