package com.first_class.msa.message.application.service;

import com.first_class.msa.message.application.dto.ResHubTransitInfoGetDTO;

public interface HubService {

    ResHubTransitInfoGetDTO getBy(Long departureHubId, Long arrivalHubId);
}
