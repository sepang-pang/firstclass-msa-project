package com.first_class.msa.orders.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqBusinessDTO {
	private Long businessId;

	public static ReqBusinessDTO from(Long businessId) {
		return ReqBusinessDTO
			.builder()
			.businessId(businessId)
			.build();
	}
}
