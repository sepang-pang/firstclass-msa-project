package com.first_class.msa.orders.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthSearchConditionDTO {
	private Long userId;
	private Long businessId;
	private Long hubId;
	private List<Long> orderIdList;

	public static AuthSearchConditionDTO createForMaster() {
		return AuthSearchConditionDTO.builder()
			.build();
	}

	public static AuthSearchConditionDTO createForHubManager(Long hubId) {
		return AuthSearchConditionDTO.builder()
			.hubId(hubId)
			.build();
	}

	public static AuthSearchConditionDTO createForBusinessManager(Long businessId) {
		return AuthSearchConditionDTO.builder()
			.businessId(businessId)
			.build();
	}

	public static AuthSearchConditionDTO createForDeliveryManager(List<Long> orderIdList) {
		return AuthSearchConditionDTO.builder()
			.orderIdList(orderIdList)
			.build();
	}
}
