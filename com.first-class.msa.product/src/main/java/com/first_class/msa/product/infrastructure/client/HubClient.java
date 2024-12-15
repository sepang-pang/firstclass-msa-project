package com.first_class.msa.product.infrastructure.client;

import com.first_class.msa.product.application.service.HubService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubClient extends HubService {

    @GetMapping("/external/hubs/{hubId}/exists")
    boolean existsBy(@PathVariable Long hubId);

    @GetMapping("/external/hubs/user/{userId}/id")
    Long getHubIdBy(@PathVariable Long userId);
}
