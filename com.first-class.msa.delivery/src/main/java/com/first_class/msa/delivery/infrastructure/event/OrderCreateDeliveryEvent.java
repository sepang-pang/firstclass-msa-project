package com.first_class.msa.delivery.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
