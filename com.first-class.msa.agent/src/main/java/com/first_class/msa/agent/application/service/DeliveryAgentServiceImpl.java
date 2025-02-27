package com.first_class.msa.agent.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.agent.application.dto.DeliveryAgentAuthSearchConditionDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentGetByUserIdDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentSearchDTO;
import com.first_class.msa.agent.application.dto.ResGlobalDeliveryAgentDTO;
import com.first_class.msa.agent.application.dto.ResHubDeliveryAgentDTO;
import com.first_class.msa.agent.application.dto.ResRoleGetByIdDTO;
import com.first_class.msa.agent.domain.common.IsAvailable;
import com.first_class.msa.agent.domain.common.Type;
import com.first_class.msa.agent.domain.common.UserRole;
import com.first_class.msa.agent.domain.entity.DeliveryAgent;
import com.first_class.msa.agent.domain.repository.DeliveryAgentRepository;
import com.first_class.msa.agent.domain.valueobject.Sequence;
import com.first_class.msa.agent.libs.exception.ApiException;
import com.first_class.msa.agent.libs.message.ErrorMessage;
import com.first_class.msa.agent.presentation.dto.ReqDeliveryAgentPostDTO;
import com.first_class.msa.agent.presentation.dto.ReqDeliveryAgentPutDTO;

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
	public ResDeliveryAgentDTO postBy(Long userId, ReqDeliveryAgentPostDTO reqDeliveryAgentPostDTO) {
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);

		Long hubId = reqDeliveryAgentPostDTO.getHubId();
		Integer maxSequence;

		authConditionService.validateCreateUserRole(
			UserRole.valueOf(resRoleGetByIdDTO.getRole()),
			hubId,
			userId
		);

		if (reqDeliveryAgentPostDTO.getType() == Type.HUB_AGENT) {
			maxSequence = deliveryAgentRepository.findMaxSequenceByHubIdAndTypeHub(hubId);
		} else {
			maxSequence = deliveryAgentRepository.findMaxSequenceByHubIdAndTypeDelivery(hubId);
		}
		Sequence sequence = new Sequence(maxSequence + 1);

		DeliveryAgent deliveryAgent = DeliveryAgent.createDeliveryAgent(
			reqDeliveryAgentPostDTO.getUserId(),
			reqDeliveryAgentPostDTO.getSlackId(),
			reqDeliveryAgentPostDTO.getHubId(),
			reqDeliveryAgentPostDTO.getType(),
			sequence
		);

		deliveryAgent.setCreatedByAndUpdateBy(reqDeliveryAgentPostDTO.getUserId());
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
		DeliveryAgent deliveryAgent = findById(deliverAgentId);
		authConditionService.validateSearchDetailUserRole(
			UserRole.valueOf(resRoleGetByIdDTO.getRole()),
			userId,
			deliveryAgent
		);
		return ResDeliveryAgentSearchDTO.DeliveryAgentDetailDTO.from(deliveryAgent);
	}

	@Override
	@Transactional(readOnly = true)
	public ResDeliveryAgentGetByUserIdDTO getDeliveryAgentByUserId(Long userId) {
		DeliveryAgent deliveryAgent = deliveryAgentRepository.findByUserId(userId).orElseThrow(
			() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_DELIVERY_AGENT)));
		return ResDeliveryAgentGetByUserIdDTO.from(deliveryAgent);
	}

	@Override
	@Transactional
	public ResGlobalDeliveryAgentDTO assignGlobalDeliveryAgent(Long hubId) {
		DeliveryAgent agent = deliveryAgentCacheService.getGlobalAgent(hubId);

		if (agent == null) {
			// 캐시에 없으면 DB에서 조회
			agent = deliveryAgentRepository.findByDeliveryIdAndHubId(hubId);

			if (agent == null) {
				throw new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_GLOBAL_DELIVERY_AGENT));
			}

			// 조회된 에이전트를 캐시에 저장
			deliveryAgentCacheService.saveGlobalAgent(hubId, agent);
		}

		return ResGlobalDeliveryAgentDTO.from(agent.getId());
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
	public ResHubDeliveryAgentDTO assignHubDeliveryAgent(Long hubId) {
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

		return ResHubDeliveryAgentDTO.from(assignedAgent.getId());
	}


	@Override
	@Transactional
	public void putBy(Long userId, Long deliveryAgentId, ReqDeliveryAgentPutDTO reqDeliveryAgentPutDTO) {
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);
		DeliveryAgent deliveryAgent = findById(deliveryAgentId);
		authConditionService.validateUpdateAndDeleteUserRole(
			UserRole.valueOf(resRoleGetByIdDTO.getRole()),
			userId,
			deliveryAgent);

		deliveryAgent.updateDeliveryAgent(
			reqDeliveryAgentPutDTO.getIsAvailable(),
			reqDeliveryAgentPutDTO.getSlackId(),
			reqDeliveryAgentPutDTO.getType(),
			userId
			);

	}

	@Override
	@Transactional
	public void deleteBy(Long userId, Long deliveryAgentId){
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);
		DeliveryAgent deliveryAgent = findById(deliveryAgentId);
		authConditionService.validateUpdateAndDeleteUserRole(
			UserRole.valueOf(resRoleGetByIdDTO.getRole()),
			userId,
			deliveryAgent);

		deliveryAgent.deleteDeliveryAgent(userId);
		updateCacheAfterDeletion(deliveryAgent);

	}

	private void updateCacheAfterDeletion(DeliveryAgent deliveryAgent) {
		if (deliveryAgent.getType().equals(Type.HUB_AGENT)) {
			DeliveryAgent agent = deliveryAgentCacheService.getGlobalAgent(deliveryAgent.getHubId());

			deliveryAgentCacheService.saveGlobalAgent(agent.getHubId(), agent);
		} else {
			Long hubId = deliveryAgent.getHubId();
			List<DeliveryAgent> agentList = deliveryAgentCacheService.getHubAgentList(hubId);
			int currentSequence = deliveryAgentCacheService.getHubSequence(hubId);

			agentList = agentList.stream()
				.filter(agent -> !agent.getId().equals(deliveryAgent.getId()))
				.collect(Collectors.toList());

			int adjustedSequence = adjustSequenceAfterDeletion(currentSequence, agentList.size());
			deliveryAgentCacheService.updateHubSequence(hubId, adjustedSequence);

			deliveryAgentCacheService.saveHubAgentList(hubId, agentList);
		}
	}

	/**
	 * 시퀀스 조정
	 */
	private int adjustSequenceAfterDeletion(int currentSequence, int listSize) {
		if (listSize == 0) {
			return 0;
		}

		return currentSequence % listSize;
	}



	private DeliveryAgent findById(Long deliveryAgentId) {
		return deliveryAgentRepository.findById(deliveryAgentId).orElseThrow(
			() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_DELIVERY_AGENT))
		);

	}

}
