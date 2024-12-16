package com.first_class.msa.agent.presentation.controller.external;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.first_class.msa.agent.application.dto.ResDeliveryAgentGetByUserIdDTO;
import com.first_class.msa.agent.application.dto.ResGlobalDeliveryAgentDTO;
import com.first_class.msa.agent.application.dto.ResHubDeliveryAgentDTO;
import com.first_class.msa.agent.application.service.DeliveryAgentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExternalAgentController {

	private final DeliveryAgentService deliveryAgentService;

	@PostMapping("/external/agents/global")
	public ResGlobalDeliveryAgentDTO assignGlobalAgent() {
		return deliveryAgentService.assignGlobalDeliveryAgent();
	}

	@PostMapping("/external/agents/hubs/{hubId}")
	public ResHubDeliveryAgentDTO assignHubAgent(
		@PathVariable Long hubId
	) {
		return deliveryAgentService.assignHubDeliveryAgent(hubId);
	}

	@GetMapping("/external/agents")
	public ResDeliveryAgentGetByUserIdDTO getDeliveryAgentByUserId(@RequestParam Long userId){
		return deliveryAgentService.getDeliveryAgentByUserId(userId);
	}

}
