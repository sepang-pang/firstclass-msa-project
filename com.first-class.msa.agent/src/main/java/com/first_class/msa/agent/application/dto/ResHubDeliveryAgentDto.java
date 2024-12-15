package com.first_class.msa.agent.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResHubDeliveryAgentDto {
	private Long deliveryAgentId;

	public static ResHubDeliveryAgentDto from(Long deliveryAgent) {
		return ResHubDeliveryAgentDto.builder()
			.deliveryAgentId(deliveryAgent)
			.build();
	}
}
