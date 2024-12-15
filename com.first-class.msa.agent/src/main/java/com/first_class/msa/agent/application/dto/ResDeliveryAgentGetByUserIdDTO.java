package com.first_class.msa.agent.application.dto;

import com.first_class.msa.agent.domain.entity.DeliveryAgent;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResDeliveryAgentGetByUserIdDTO {
	private Long deliveryAgentId;

	public static ResDeliveryAgentGetByUserIdDTO from(DeliveryAgent deliveryAgent){
		return ResDeliveryAgentGetByUserIdDTO
			.builder()
			.deliveryAgentId(deliveryAgent.getId())
			.build();
	}
}
