package com.first_class.msa.orders.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeleteDeliveryEvent {
	private Long orderId;
	private Long userId;
}
