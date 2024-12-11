package com.first_class.msa.business.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/auth/{userId}")
    String getRoleBy(@PathVariable(name = "userId") Long userId);
}
