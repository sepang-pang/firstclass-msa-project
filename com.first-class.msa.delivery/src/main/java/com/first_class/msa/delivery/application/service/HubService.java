package com.first_class.msa.delivery.application.service;

import com.first_class.msa.delivery.application.dto.ResHubInfoGetDTO;
import com.first_class.msa.delivery.application.dto.ResHubTransitInfoGetDTO;

public interface HubService {

	boolean existsBy(Long hubId);

	Long getHubIdBy(Long userId);

	ResHubTransitInfoGetDTO postHubTransitInfo(Long departureHubId, Long arrivalHubId);

	ResHubInfoGetDTO getHubInfoBy(Long hubId);

}
