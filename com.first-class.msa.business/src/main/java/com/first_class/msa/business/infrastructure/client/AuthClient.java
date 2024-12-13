package com.first_class.msa.business.infrastructure.client;

import com.first_class.msa.business.application.dto.ResRoleGetByUserIdDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/auth/{userId}")
    ResRoleGetByUserIdDTO getRoleBy(@PathVariable(name = "userId") Long userId);
}
