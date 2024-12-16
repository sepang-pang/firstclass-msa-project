package com.first_class.msa.hub.infrastructure.client;

import com.first_class.msa.hub.application.dto.external.ExternalResRoleGetByUserIdDTO;
import com.first_class.msa.hub.application.service.AuthService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient extends AuthService {

    @GetMapping("/external/users/{userId}")
    ExternalResRoleGetByUserIdDTO getRoleBy(@PathVariable(name = "userId") Long userId);
}
