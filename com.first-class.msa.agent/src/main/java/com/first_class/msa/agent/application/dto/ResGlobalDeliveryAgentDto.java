package com.first_class.msa.agent.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResGlobalDeliveryAgentDto {
	private Long deliveryAgent;

	public static ResGlobalDeliveryAgentDto from(Long deliveryAgent) {
		return ResGlobalDeliveryAgentDto.builder()
			.deliveryAgent(deliveryAgent)
			.build();
	}
}
