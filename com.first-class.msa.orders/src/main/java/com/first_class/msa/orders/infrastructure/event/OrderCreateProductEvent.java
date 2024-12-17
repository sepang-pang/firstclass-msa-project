package com.first_class.msa.orders.infrastructure.event;

import java.util.List;

import com.first_class.msa.orders.application.dto.ResOrderDTO;
import com.first_class.msa.orders.domain.model.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreateProductEvent {
	private Long orderId;
	private List<ResOrderDTO.OrderDTO.OrderLineDTO> orderLineDTO;

	public static OrderCreateProductEvent from(Order order){
		return OrderCreateProductEvent.builder()
			.orderId(order.getId())
			.orderLineDTO(ResOrderDTO.OrderDTO.OrderLineDTO.from(order.getOrderLineList()))
			.build();
	}


}
