package com.first_class.msa.delivery.application.service;

import com.first_class.msa.delivery.application.dto.ResDeliveryAgentGetByUserIdDTO;
import com.first_class.msa.delivery.application.dto.ResGlobalDeliveryAgentDTO;
import com.first_class.msa.delivery.application.dto.ResHubDeliveryAgentDTO;

public interface AgentService {

	ResGlobalDeliveryAgentDTO assignGlobalAgent(Long hubId);

	ResHubDeliveryAgentDTO assignHubAgent(Long hubId);

	ResDeliveryAgentGetByUserIdDTO getDeliveryAgentByUserId(Long userId);
}
