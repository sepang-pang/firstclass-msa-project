package com.first_class.msa.agent.application.dto;

import com.first_class.msa.agent.domain.common.IsAvailable;
import com.first_class.msa.agent.domain.common.Type;
import com.first_class.msa.agent.domain.entity.DeliveryAgent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResDeliveryAgentDTO {
	private DeliveryAgentDTO deliveryAgentDTO;

	public static ResDeliveryAgentDTO from(DeliveryAgent deliveryAgent){
		return ResDeliveryAgentDTO.builder()
			.deliveryAgentDTO(DeliveryAgentDTO.from(deliveryAgent))
		.build();
	}


	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DeliveryAgentDTO{
		private Long deliveryAgentId;
		private Long userId;
		private Long hubId;
		private String slackId;
		private Type type;
		private IsAvailable isAvailable;

		public static DeliveryAgentDTO from(DeliveryAgent deliveryAgent){
			return DeliveryAgentDTO.builder()
				.deliveryAgentId(deliveryAgent.getId())
				.userId(deliveryAgent.getUserId())
				.hubId(deliveryAgent.getHubId())
				.slackId(deliveryAgent.getSlackId())
				.type(deliveryAgent.getType())
				.isAvailable(deliveryAgent.getIsAvailable())
				.build();
		}

	}

}
