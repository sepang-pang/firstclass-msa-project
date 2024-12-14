package com.first_class.msa.agent.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.agent.application.dto.DeliveryAgentAuthSearchConditionDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentSearchDTO;
import com.first_class.msa.agent.application.dto.ResGlobalDeliveryAgentDto;
import com.first_class.msa.agent.application.dto.ResHubDeliveryAgentDto;
import com.first_class.msa.agent.application.dto.ResRoleGetByIdDTO;
import com.first_class.msa.agent.domain.common.IsAvailable;
import com.first_class.msa.agent.domain.common.Type;
import com.first_class.msa.agent.domain.common.UserRole;
import com.first_class.msa.agent.domain.entity.DeliveryAgent;
import com.first_class.msa.agent.domain.repository.DeliveryAgentRepository;
import com.first_class.msa.agent.domain.valueobject.Sequence;
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
	private final DeliveryAgentCacheService deliveryAgentCacheService;

	@Override
	@Transactional
	public ResDeliveryAgentDTO postBy(Long userId, ReqDeliveryAgentDTO reqDeliveryAgentDTO) {
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);

		Long hubId = reqDeliveryAgentDTO.getHubId();
		Integer maxSequence;
		Sequence sequence;

		DeliveryAgent deliveryAgent;

		if (reqDeliveryAgentDTO.getType().equals(Type.HUB_AGENT)) {
			hubId = null;
		}

		authConditionService.validateCreateUserRole(
			UserRole.valueOf(resRoleGetByIdDTO.getRole()),
			hubId,
			userId
		);

		if (hubId != null) {
			maxSequence = deliveryAgentRepository.findMaxSequenceByHubId(hubId);
		} else {
			maxSequence = deliveryAgentRepository.findMaxSequenceByHubIdIsNull();
		}
		sequence = new Sequence(maxSequence + 1);

		deliveryAgent = DeliveryAgent.createDeliveryAgent(
			userId,
			reqDeliveryAgentDTO.getSlackId(),
			hubId,
			reqDeliveryAgentDTO.getType(),
			sequence
		);

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

	/**
	 * 허브간 배송 허브에 국한되지 않고 사용하기 때문에 global로 표시
	 * 1. Redis에서 허브 간 배송 담당자 목록 가져오기
	 * 2. 캐시에 없으면 DB에서 가져와 캐시에 저장
	 * 3. Redis에서 현재 순번 가져오기
	 * 4. 현재 순번에 해당하는 담당자 배정
	 * 5. 다음 순번 계산 및 Redis에 업데이트
	 */
	@Override
	@Transactional
	public ResGlobalDeliveryAgentDto assignGlobalDeliveryAgent() {
		List<DeliveryAgent> agentList = deliveryAgentCacheService.getGlobalAgentList();
		if (agentList == null || agentList.isEmpty()) {

			agentList = deliveryAgentRepository.findByHubIdIsNull();
			if (agentList.isEmpty()) {
				throw new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_GLOBAL_DELIVERY_AGENT_LIST));
			}
			deliveryAgentCacheService.saveGlobalAgentList(agentList);
		}

		List<DeliveryAgent> activeAgentList = agentList.stream()
			.filter(agent -> agent.getIsAvailable() == IsAvailable.ENABLE)
			.toList();

		if (activeAgentList.isEmpty()) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.NO_ACTIVE_GLOBAL_DELIVERY_AGENT));
		}

		int currentSequence = deliveryAgentCacheService.getGlobalSequence();

		DeliveryAgent assignedAgent = agentList.get(currentSequence);

		int nextSequence = (currentSequence + 1) % agentList.size();
		deliveryAgentCacheService.updateGlobalSequence(nextSequence);

		return ResGlobalDeliveryAgentDto.from(assignedAgent.getId());
	}

	/**
	 * 허브 업체간 배송관리자
	 * 1. Redis에서 허브 업체 간 배송 담당자 목록 가져오기
	 * 2. 캐시에 없으면 DB에서 가져와 캐시에 저장
	 * 3. Redis에서 현재 순번 가져오기
	 * 4. 현재 순번에 해당하는 담당자 배정
	 * 5. 다음 순번 계산 및 Redis에 업데이트
	 */

	@Override
	@Transactional
	public ResHubDeliveryAgentDto assignHubDeliveryAgent(Long hubId) {
		List<DeliveryAgent> agentList = deliveryAgentCacheService.getHubAgentList(hubId);
		if (agentList == null || agentList.isEmpty()) {
			agentList = deliveryAgentRepository.findByHubId(hubId);
			if (agentList.isEmpty()) {
				throw new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_HUB_DELIVERY_AGENT_LIST));
			}
			deliveryAgentCacheService.saveHubAgentList(hubId, agentList);
		}

		List<DeliveryAgent> activeAgentList = agentList.stream()
			.filter(agent -> agent.getIsAvailable() == IsAvailable.ENABLE)
			.toList();

		if (activeAgentList.isEmpty()) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.NO_ACTIVE_HUB_DELIVERY_AGENT));
		}

		int currentSequence = deliveryAgentCacheService.getHubSequence(hubId);

		DeliveryAgent assignedAgent = agentList.get(currentSequence);

		int nextSequence = (currentSequence + 1) % agentList.size();
		deliveryAgentCacheService.updateHubSequence(hubId, nextSequence);

		return ResHubDeliveryAgentDto.from(assignedAgent.getId());
	}

	// TODO: 2024-12-14 배송 관리자 삭제 및 수정 추가

	@Override
	@Transactional
	public void putBy(){

	}

	// 추후 삭제 추가시 로직

	// @Override
	// @Transactional
	// public void updateGlobalDeliveryAgents(List<DeliveryAgent> updatedAgents) {
	// 	deliveryAgentCacheService.saveGlobalAgentList(updatedAgents);
	// }
	//
	// @Override
	// @Transactional
	// public void updateHubDeliveryAgents(Long hubId, List<DeliveryAgent> updatedAgents) {
	// 	deliveryAgentCacheService.saveGlobalAgentList(hubId, updatedAgents);
	// }


}
