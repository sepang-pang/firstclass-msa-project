package com.first_class.msa.agent.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResHubDeliveryAgentDto {
	private Long deliveryAgent;

	public static ResHubDeliveryAgentDto from(Long deliveryAgent) {
		return ResHubDeliveryAgentDto.builder()
			.deliveryAgent(deliveryAgent)
			.build();
	}
}
