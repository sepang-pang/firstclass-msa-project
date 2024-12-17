package com.first_class.msa.message.infrastructure.client;

import com.first_class.msa.message.application.dto.ResHubTransitInfoGetDTO;
import com.first_class.msa.message.application.service.HubService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hub-service")
public interface HubClient extends HubService {

    @GetMapping("/external/hubs/hub-transit-infos")
    public ResHubTransitInfoGetDTO getBy(@RequestParam(name = "departureHubId") Long departureHubId,
                                         @RequestParam(name = "arrivalHubId") Long arrivalHubId);
}
