package com.first_class.msa.orders.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
