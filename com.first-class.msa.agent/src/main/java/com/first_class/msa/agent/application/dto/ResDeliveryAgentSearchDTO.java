package com.first_class.msa.agent.application.dto;

import org.springframework.data.domain.Page;

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
public class ResDeliveryAgentSearchDTO {
	private Page<DeliveryAgentDetailDTO> pageDeliveryAgent;

	public static ResDeliveryAgentSearchDTO from(Page<DeliveryAgentDetailDTO> pageDeliveryAgent){
		return ResDeliveryAgentSearchDTO.builder()
			.pageDeliveryAgent(pageDeliveryAgent)
			.build();
	}


	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DeliveryAgentDetailDTO{
		private Long deliverAgentId;
		private Long userId;
		private Long hubId;
		private String slackId;
		private Type type;
		private int sequence;

		public static  DeliveryAgentDetailDTO from(DeliveryAgent deliveryAgent){
			return DeliveryAgentDetailDTO.builder()
				.deliverAgentId(deliveryAgent.getId())
				.userId(deliveryAgent.getUserId())
				.hubId(deliveryAgent.getHubId())
				.slackId(deliveryAgent.getSlackId())
				.type(deliveryAgent.getType())
				.sequence(deliveryAgent.getSequence().getValue())
				.build();
		}




	}



}
