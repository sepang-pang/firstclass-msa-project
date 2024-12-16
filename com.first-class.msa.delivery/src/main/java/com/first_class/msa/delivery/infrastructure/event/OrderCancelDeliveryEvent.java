package com.first_class.msa.delivery.infrastructure.event;

import lombok.Getter;

@Getter
public class OrderCancelDeliveryEvent {
	private Long userId;
	private Long orderId;

}
