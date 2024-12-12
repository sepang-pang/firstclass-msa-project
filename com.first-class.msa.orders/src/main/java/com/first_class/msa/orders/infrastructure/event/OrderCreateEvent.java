package com.first_class.msa.orders.infrastructure.event;

import java.util.List;

import com.first_class.msa.orders.application.dto.ResOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateEvent {
	private Long orderId;
	private Long userId;
	private List<ResOrderDTO.OrderDTO.OrderLineDTO> orderLineDTO;


}
