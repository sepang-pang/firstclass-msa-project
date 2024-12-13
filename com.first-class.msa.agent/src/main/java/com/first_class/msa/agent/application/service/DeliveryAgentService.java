package com.first_class.msa.agent.application.service;

import com.first_class.msa.agent.application.dto.ResDeliveryAgentDTO;
import com.first_class.msa.agent.presentation.dto.ReqDeliveryAgentDTO;

public interface DeliveryAgentService {

	ResDeliveryAgentDTO postBy(Long userId, ReqDeliveryAgentDTO reqDeliveryAgentDTO);
}
