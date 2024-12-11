package com.first_class.msa.hub.infrastructure.client;

import com.first_class.msa.hub.application.dto.ReqRoleValidationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @PostMapping("/auth/{userId}")
    boolean checkBy(@PathVariable(name = "userId") Long userId, @RequestBody ReqRoleValidationDTO dto);
}
