package com.first_class.msa.orders.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.first_class.msa.orders.application.service.HubService;

@FeignClient(name = "hub-service")
public interface HubClient extends HubService {
}
