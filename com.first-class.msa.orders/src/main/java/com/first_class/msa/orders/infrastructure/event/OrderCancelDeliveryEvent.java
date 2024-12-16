package com.first_class.msa.orders.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCancelDeliveryEvent {
	private Long userId;
	private Long orderId;

	public static OrderCancelDeliveryEvent of(Long userId, Long orderId){
		return OrderCancelDeliveryEvent.builder()
			.userId(userId)
			.orderId(orderId)
			.build();
	}
}
