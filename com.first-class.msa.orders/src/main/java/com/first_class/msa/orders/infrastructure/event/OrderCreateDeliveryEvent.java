package com.first_class.msa.orders.infrastructure.event;

import com.first_class.msa.orders.domain.model.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreateDeliveryEvent {
	private Long userId;
	private Long orderId;
	private Long departureHubId;
	private Long deliveryBusinessId;
	private Long arrivalHubId;
	private String address;

	public static OrderCreateDeliveryEvent from(Order order){
		return  OrderCreateDeliveryEvent
			.builder()
			.userId(order.getUserId())
			.orderId(order.getId())
			.departureHubId(order.getHubId())
			.deliveryBusinessId(order.getDeliveryBusinessId())
			.arrivalHubId(order.getArrivalHubId())
			.build();
	}
}
