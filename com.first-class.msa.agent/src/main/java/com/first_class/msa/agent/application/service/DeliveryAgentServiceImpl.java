package com.first_class.msa.agent.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.agent.application.dto.ResDeliveryAgentDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentSearchDTO;
import com.first_class.msa.agent.application.dto.ResRoleGetByIdDTO;
import com.first_class.msa.agent.domain.common.UserRole;
import com.first_class.msa.agent.domain.entity.DeliveryAgent;
import com.first_class.msa.agent.domain.repository.DeliveryAgentRepository;
import com.first_class.msa.agent.presentation.dto.ReqDeliveryAgentDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryAgentServiceImpl implements DeliveryAgentService {
	private final DeliveryAgentRepository deliveryAgentRepository;
	private final AuthConditionService authConditionService;
	private final AuthService authService;

	@Override
	@Transactional
	public ResDeliveryAgentDTO postBy(Long userId, ReqDeliveryAgentDTO reqDeliveryAgentDTO){
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);

		authConditionService.validateCreateUserRole(
			UserRole.valueOf(resRoleGetByIdDTO.getRole()),
			reqDeliveryAgentDTO.getHubId(),
			userId
		);

		DeliveryAgent deliveryAgent
			= DeliveryAgent.createDeliveryAgent(
				userId,
			reqDeliveryAgentDTO.getSlackId(),
			reqDeliveryAgentDTO.getHubId(),
			reqDeliveryAgentDTO.getType());


		deliveryAgent = deliveryAgentRepository.save(deliveryAgent);
		return ResDeliveryAgentDTO.from(deliveryAgent);
	}

	@Transactional(readOnly = true)
	public ResDeliveryAgentSearchDTO getSearchDeliveryAgentBy(Long userid){
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userid);


	}



}
