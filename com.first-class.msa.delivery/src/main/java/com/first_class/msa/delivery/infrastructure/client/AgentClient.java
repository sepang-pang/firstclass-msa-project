package com.first_class.msa.delivery.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.first_class.msa.delivery.application.dto.ResDeliveryAgentGetByUserIdDTO;
import com.first_class.msa.delivery.application.dto.ResGlobalDeliveryAgentDTO;
import com.first_class.msa.delivery.application.dto.ResHubDeliveryAgentDTO;
import com.first_class.msa.delivery.application.service.AgentService;

@FeignClient(name = "agent-service")
public interface AgentClient extends AgentService {

	@PostMapping("/external/global")
	ResGlobalDeliveryAgentDTO assignGlobalAgent();

	@PostMapping("/external/hub/{hubId}")
	ResHubDeliveryAgentDTO assignHubAgent(@PathVariable Long hubId);

	@GetMapping("/external/agents")
	ResDeliveryAgentGetByUserIdDTO getDeliveryAgentByUserId(@RequestParam Long userId);



}
