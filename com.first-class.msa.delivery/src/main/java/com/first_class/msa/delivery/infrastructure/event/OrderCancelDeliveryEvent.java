package com.first_class.msa.delivery.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelDeliveryEvent {
	private Long userId;
	private Long orderId;

}
