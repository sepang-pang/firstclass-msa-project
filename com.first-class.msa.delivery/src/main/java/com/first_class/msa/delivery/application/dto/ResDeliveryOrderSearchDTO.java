package com.first_class.msa.delivery.application.dto;

import java.util.List;

import com.first_class.msa.delivery.domain.model.Delivery;

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

	public static ResDeliveryOrderSearchDTO from(List<Delivery> deliveryList){
		return ResDeliveryOrderSearchDTO.builder()
			.orderIdList(deliveryList.stream().map(Delivery::getOrderId).toList())
			.build();
	}
}
