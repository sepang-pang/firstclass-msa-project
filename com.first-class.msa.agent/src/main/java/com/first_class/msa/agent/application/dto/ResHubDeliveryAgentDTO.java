package com.first_class.msa.agent.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResHubDeliveryAgentDTO {
	private Long deliveryAgentId;

	public static ResHubDeliveryAgentDTO from(Long deliveryAgent) {
		return ResHubDeliveryAgentDTO.builder()
			.deliveryAgentId(deliveryAgent)
			.build();
	}
}
