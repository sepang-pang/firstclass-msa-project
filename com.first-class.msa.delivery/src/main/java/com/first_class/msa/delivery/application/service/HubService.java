package com.first_class.msa.delivery.application.service;

import com.first_class.msa.delivery.application.dto.ResHubTransitInfoDTO;

public interface HubService {
	ResHubTransitInfoDTO postHubTransitInfo(Long departureHubId, Long arrivalHubId);

}
