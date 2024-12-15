package com.first_class.msa.delivery.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.first_class.msa.delivery.application.dto.ResHubTransitInfoGetDTO;
import com.first_class.msa.delivery.application.service.HubService;

@FeignClient(name = "hub-service")
public interface HubClient extends HubService {

	@GetMapping("/external/hubs/{hubId}/exists")
	boolean existsBy(@PathVariable(name = "hubId") Long hubId);

	@GetMapping("/external/hubs/user/{userId}/id")
	Long getHubIdBy(@PathVariable Long userId);

	@GetMapping("/external/hubs/hub-transit-infos")
	ResHubTransitInfoGetDTO getBy(@RequestParam Long departureHubId, @RequestParam Long arrivalHubId);

}
