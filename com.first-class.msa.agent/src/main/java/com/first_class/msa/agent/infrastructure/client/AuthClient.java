package com.first_class.msa.agent.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.first_class.msa.agent.application.dto.ResRoleGetByIdDTO;
import com.first_class.msa.agent.application.service.AuthService;

@FeignClient(name = "auth-service")
public interface AuthClient extends AuthService {
	@GetMapping("/auth/{userId}")
	ResRoleGetByIdDTO getRoleBy(@PathVariable Long userId);
}
