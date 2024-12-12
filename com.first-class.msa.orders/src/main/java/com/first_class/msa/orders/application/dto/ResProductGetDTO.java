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
public class ResProductGetDTO {

	private Long productId;
	private Integer count;
	private Integer supplyPrice;
	private Long hubId;

	public static ResProductGetDTO from(Long productId, Integer count, Integer supplyPrice, Long hubId) {
		return ResProductGetDTO.builder()
			.productId(productId)
			.count(count)
			.supplyPrice(supplyPrice)
			.hubId(hubId)
			.build();
	}
}
