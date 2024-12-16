package com.first_class.msa.delivery.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.first_class.msa.delivery.application.dto.ResGlobalDeliveryAgentDTO;

@FeignClient(name = "agent-service")
public interface DeliveryAgentClient {

	@PostMapping("/external/agents/global")
	ResGlobalDeliveryAgentDTO assignGlobalAgent();

}
