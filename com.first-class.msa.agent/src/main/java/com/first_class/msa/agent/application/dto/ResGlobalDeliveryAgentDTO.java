package com.first_class.msa.agent.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResGlobalDeliveryAgentDTO {
	private Long deliveryAgentId;

	public static ResGlobalDeliveryAgentDTO from(Long deliveryAgentId) {
		return ResGlobalDeliveryAgentDTO.builder()
			.deliveryAgentId(deliveryAgentId)
			.build();
	}
}
