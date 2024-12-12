package com.first_class.msa.business.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubClient {

    @GetMapping("/hubs/{hubId}/exists")
    boolean existsBy(@PathVariable Long hubId);

    @GetMapping("/hub/by-user/{userId}")
    Long getHubIdBy(@PathVariable Long userId);
}
