package com.first_class.msa.orders.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeleteDeliveryEvent {
	private Long orderId;
	private Long userId;
}
