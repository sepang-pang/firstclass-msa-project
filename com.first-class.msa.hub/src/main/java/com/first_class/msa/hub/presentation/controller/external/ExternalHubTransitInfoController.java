package com.first_class.msa.hub.presentation.controller.external;

import com.first_class.msa.hub.application.dto.transit.ResHubTransitInfoGetDTO;
import com.first_class.msa.hub.application.service.HubTransitInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExternalHubTransitInfoController {

    private final HubTransitInfoService hubTransitInfoService;

    @GetMapping("/external/hubs/hub-transit-infos/hub-transit-infos")
    public ResHubTransitInfoGetDTO getBy(@RequestParam(name = "departureHubId") Long departureHubId,
                                         @RequestParam(name = "arrivalHubId") Long arrivalHubId) {

        return hubTransitInfoService.getBy(departureHubId, arrivalHubId);
    }
}
