package com.first_class.msa.delivery.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDeliveryEvent {
	private Long userId;
	private Long orderId;
	private Long departureHubId;
	private Long deliveryBusinessId;
	private Long arrivalHubId;
	private String address;

}
