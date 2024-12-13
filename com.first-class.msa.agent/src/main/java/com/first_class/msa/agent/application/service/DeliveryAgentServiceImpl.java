package com.first_class.msa.agent.application.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.agent.application.dto.DeliveryAgentAuthSearchConditionDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentSearchDTO;
import com.first_class.msa.agent.application.dto.ResRoleGetByIdDTO;
import com.first_class.msa.agent.domain.common.IsAvailable;
import com.first_class.msa.agent.domain.common.Type;
import com.first_class.msa.agent.domain.common.UserRole;
import com.first_class.msa.agent.domain.entity.DeliveryAgent;
import com.first_class.msa.agent.domain.repository.DeliveryAgentRepository;
import com.first_class.msa.agent.libs.exception.ApiException;
import com.first_class.msa.agent.libs.message.ErrorMessage;
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
	public ResDeliveryAgentDTO postBy(Long userId, ReqDeliveryAgentDTO reqDeliveryAgentDTO) {
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);

		Long hubId = reqDeliveryAgentDTO.getHubId();
		DeliveryAgent deliveryAgent;
		if (reqDeliveryAgentDTO.getType().equals(Type.HUB_AGENT)) {
			hubId = null;
		}

		authConditionService.validateCreateUserRole(
			UserRole.valueOf(resRoleGetByIdDTO.getRole()),
			hubId,
			userId
		);

		deliveryAgent = DeliveryAgent.createDeliveryAgent(
			userId,
			reqDeliveryAgentDTO.getSlackId(),
			reqDeliveryAgentDTO.getHubId(),
			reqDeliveryAgentDTO.getType());

		deliveryAgent = deliveryAgentRepository.save(deliveryAgent);
		return ResDeliveryAgentDTO.from(deliveryAgent);
	}

	@Override
	@Transactional(readOnly = true)
	public ResDeliveryAgentSearchDTO getSearchDeliveryAgentBy(
		Long userId,
		Long hubId,
		Type type,
		IsAvailable isAvailable,
		Pageable pageable
	) {
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);

		authConditionService.validateSearchUserRole(UserRole.valueOf(resRoleGetByIdDTO.getRole()), userId, hubId);
		DeliveryAgentAuthSearchConditionDTO deliveryAgentAuthSearchConditionDTO
			= SearchConditionByUserRole(UserRole.valueOf(resRoleGetByIdDTO.getRole()), hubId, type, isAvailable);

		return deliveryAgentRepository.findByAllDeliveryAgent(deliveryAgentAuthSearchConditionDTO, pageable);
	}

	private DeliveryAgentAuthSearchConditionDTO SearchConditionByUserRole(
		UserRole userRole,
		Long hubId,
		Type type,
		IsAvailable isAvailable
	) {
		DeliveryAgentAuthSearchConditionDTO deliveryAgentAuthSearchConditionDTO = null;
		switch (userRole) {
			case MASTER -> deliveryAgentAuthSearchConditionDTO
				= DeliveryAgentAuthSearchConditionDTO.createForMaster(hubId, type, isAvailable);

			case HUB_MANAGER -> deliveryAgentAuthSearchConditionDTO
				= DeliveryAgentAuthSearchConditionDTO.createForHubManager(hubId, type, isAvailable);
		}
		return deliveryAgentAuthSearchConditionDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public ResDeliveryAgentSearchDTO.DeliveryAgentDetailDTO getDeliveryAgentById(Long userId, Long deliverAgentId) {
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);
		DeliveryAgent deliveryAgent = deliveryAgentRepository.findById(deliverAgentId).orElseThrow(
			() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_DELIVERY_AGENT))
		);
		authConditionService.validateSearchDetailUserRole(
			UserRole.valueOf(resRoleGetByIdDTO.getRole()),
			userId,
			deliveryAgent
		);
		return ResDeliveryAgentSearchDTO.DeliveryAgentDetailDTO.from(deliveryAgent);
	}


}
