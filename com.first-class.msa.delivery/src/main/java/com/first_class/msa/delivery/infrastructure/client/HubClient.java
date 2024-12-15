package com.first_class.msa.delivery.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.first_class.msa.delivery.application.dto.ResHubTransitInfoDTO;
import com.first_class.msa.delivery.application.service.HubService;

@FeignClient(name = "hub-service")
public interface HubClient extends HubService {

	@PostMapping("/external/hubs")
	ResHubTransitInfoDTO postHubTransitInfo(@RequestParam Long departureHubId, @RequestParam Long arrivalHubId);


}
