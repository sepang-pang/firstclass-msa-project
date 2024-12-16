package com.first_class.msa.delivery.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResDeliveryOrderSearchDTO {
	private List<Long> orderIdList;

	public static ResDeliveryOrderSearchDTO from(List<Long> deliveryOrderList){
		return ResDeliveryOrderSearchDTO.builder()
			.orderIdList(deliveryOrderList)
			.build();
	}
}
