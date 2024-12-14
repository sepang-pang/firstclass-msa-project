package com.first_class.msa.agent.application.service;

import org.springframework.data.domain.Pageable;

import com.first_class.msa.agent.application.dto.ResDeliveryAgentDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentSearchDTO;
import com.first_class.msa.agent.application.dto.ResGlobalDeliveryAgentDto;
import com.first_class.msa.agent.application.dto.ResHubDeliveryAgentDto;
import com.first_class.msa.agent.domain.common.IsAvailable;
import com.first_class.msa.agent.domain.common.Type;
import com.first_class.msa.agent.presentation.dto.ReqDeliveryAgentPostDTO;
import com.first_class.msa.agent.presentation.dto.ReqDeliveryAgentPutDTO;

public interface DeliveryAgentService {

	ResDeliveryAgentDTO postBy(Long userId, ReqDeliveryAgentPostDTO reqDeliveryAgentPostDTO);

	ResDeliveryAgentSearchDTO getSearchDeliveryAgentBy(
		Long userid,
		Long hubId ,
		Type type,
		IsAvailable isAvailable,
		Pageable pageable
	);

	ResDeliveryAgentSearchDTO.DeliveryAgentDetailDTO getDeliveryAgentById(Long userId, Long deliverAgentId);
	ResGlobalDeliveryAgentDto assignGlobalDeliveryAgent();
	ResHubDeliveryAgentDto assignHubDeliveryAgent(Long hubId);

	void putBy(Long userId, Long deliveryAgentId, ReqDeliveryAgentPutDTO reqDeliveryAgentPutDTO);
}
