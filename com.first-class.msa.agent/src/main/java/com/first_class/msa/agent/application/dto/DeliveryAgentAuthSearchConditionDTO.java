package com.first_class.msa.agent.application.dto;

import com.first_class.msa.agent.domain.common.IsAvailable;
import com.first_class.msa.agent.domain.common.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAgentAuthSearchConditionDTO {
	private Long hubId;
	private Type type;
	private IsAvailable isAvailable;

	public static DeliveryAgentAuthSearchConditionDTO createForMaster(
		Long hubId,
		Type type,
		IsAvailable isAvailable
	){
		return DeliveryAgentAuthSearchConditionDTO.builder()
			.hubId(hubId)
			.type(type)
			.isAvailable(isAvailable)
			.build();
	}

	public static DeliveryAgentAuthSearchConditionDTO createForHubManager(
		Long hubId,
		Type type,
		IsAvailable isAvailable){

		return DeliveryAgentAuthSearchConditionDTO.builder()
			.hubId(hubId)
			.type(type)
			.isAvailable(isAvailable)
			.build();
	}

}
