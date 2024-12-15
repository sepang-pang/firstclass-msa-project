package com.first_class.msa.agent.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResGlobalDeliveryAgentDTO {
	private Long deliveryAgent;

	public static ResGlobalDeliveryAgentDTO from(Long deliveryAgent) {
		return ResGlobalDeliveryAgentDTO.builder()
			.deliveryAgent(deliveryAgent)
			.build();
	}
}
