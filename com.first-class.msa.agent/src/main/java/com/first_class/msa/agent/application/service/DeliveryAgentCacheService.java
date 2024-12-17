package com.first_class.msa.agent.application.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.first_class.msa.agent.domain.entity.DeliveryAgent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryAgentCacheService {
	private final RedisTemplate<String, Object> redisTemplate;

	private static final String GLOBAL_AGENTS_KEY = "delivery_agents:global";
	private static final String HUB_AGENTS_KEY_PREFIX = "delivery_agents:";
	private static final String GLOBAL_SEQUENCE_KEY = "delivery_sequence:global";
	private static final String HUB_SEQUENCE_KEY_PREFIX = "delivery_sequence:";

	private final ObjectMapper objectMapper;


	public DeliveryAgent getGlobalAgent(Long hubId) {
		Object data = redisTemplate.opsForValue().get(GLOBAL_AGENTS_KEY + hubId);
		if (data == null) {
			return null;
		}
		return objectMapper.convertValue(data, DeliveryAgent.class);
	}

	public void saveGlobalAgent(Long hubId, DeliveryAgent agent) {
		redisTemplate.opsForValue().set(GLOBAL_AGENTS_KEY + hubId, agent);
	}

	public void clearHubAgent(Long hubId) {
		redisTemplate.delete(GLOBAL_AGENTS_KEY + hubId);
	}


	public List<DeliveryAgent> getHubAgentList(Long hubId) {
		Object data = redisTemplate.opsForValue().get(HUB_AGENTS_KEY_PREFIX + hubId);
		if (data == null) {
			return Collections.emptyList();
		}
		return objectMapper.convertValue(data, new TypeReference<List<DeliveryAgent>>() {});
	}


	public void saveHubAgentList(Long hubId, List<DeliveryAgent> agentList) {
		redisTemplate.opsForValue().set(HUB_AGENTS_KEY_PREFIX + hubId, agentList);
	}


	public void clearHubAgentList(Long hubId) {
		redisTemplate.delete(HUB_AGENTS_KEY_PREFIX + hubId);
	}


	public int getGlobalSequence() {
		return Optional.ofNullable((Integer) redisTemplate.opsForValue().get(GLOBAL_SEQUENCE_KEY))
			.orElse(0);
	}


	public void updateGlobalSequence(int nextSequence) {
		redisTemplate.opsForValue().set(GLOBAL_SEQUENCE_KEY, nextSequence);
	}


	public int getHubSequence(Long hubId) {
		return Optional.ofNullable((Integer) redisTemplate.opsForValue().get(HUB_SEQUENCE_KEY_PREFIX + hubId))
			.orElse(0);
	}

	// 허브 업체 간 배송 담당자의 다음 순번 업데이트
	public void updateHubSequence(Long hubId, int nextSequence) {
		redisTemplate.opsForValue().set(HUB_SEQUENCE_KEY_PREFIX + hubId, nextSequence);
	}
}
