package com.first_class.msa.delivery.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.first_class.msa.delivery.application.dto.ResRoleGetByIdDTO;
import com.first_class.msa.delivery.application.service.AuthService;

@FeignClient(name = "auth-service")
public interface AuthClient extends AuthService {
	@GetMapping("/external/users/{userId}")
	ResRoleGetByIdDTO getRoleBy(@PathVariable Long userId);
}
