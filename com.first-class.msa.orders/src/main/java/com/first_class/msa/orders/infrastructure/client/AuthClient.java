package com.first_class.msa.orders.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.first_class.msa.orders.application.dto.ReqRoleValidationDTO;
import com.first_class.msa.orders.application.service.AuthService;
@FeignClient(name = "business-service")
public interface AuthClient extends AuthService {
	@PostMapping("/auth/{userId}")
	boolean checkBy(@PathVariable(name = "userId") Long userId, @RequestBody ReqRoleValidationDTO dto);
}
