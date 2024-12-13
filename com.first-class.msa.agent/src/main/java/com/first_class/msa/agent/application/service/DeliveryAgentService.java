package com.first_class.msa.agent.application.service;

import org.springframework.data.domain.Pageable;

import com.first_class.msa.agent.application.dto.ResDeliveryAgentDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentSearchDTO;
import com.first_class.msa.agent.domain.common.IsAvailable;
import com.first_class.msa.agent.domain.common.Type;
import com.first_class.msa.agent.presentation.dto.ReqDeliveryAgentDTO;

public interface DeliveryAgentService {

	ResDeliveryAgentDTO postBy(Long userId, ReqDeliveryAgentDTO reqDeliveryAgentDTO);

	ResDeliveryAgentSearchDTO getSearchDeliveryAgentBy(
		Long userid,
		Long hubId ,
		Type type,
		IsAvailable isAvailable,
		Pageable pageable
	);
}
