package com.first_class.msa.hub.presentation.controller.external;

import com.first_class.msa.hub.application.dto.external.ExternalResHubGetByIdDTO;
import com.first_class.msa.hub.application.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExternalHubController {

    private final HubService hubService;

    @GetMapping("/external/hubs/{hubId}/exists")
    public boolean existsBy(@PathVariable(name = "hubId") Long hubId) {
        return hubService.existsBy(hubId);
    }

    @GetMapping("/external/hubs/user/{userId}/id")
    public Long getHubIdBy(@PathVariable Long userId) {
        return hubService.getHubIdBy(userId);
    }

    @GetMapping("/external/hubs/{hubId}")
    ExternalResHubGetByIdDTO getHubBy(@PathVariable Long hubId) {
        return hubService.getBy(hubId);
    }
}
