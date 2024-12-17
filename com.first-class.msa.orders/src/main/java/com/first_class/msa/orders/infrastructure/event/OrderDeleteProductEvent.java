package com.first_class.msa.orders.infrastructure.event;

import java.util.List;

import com.first_class.msa.orders.application.dto.ResOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeleteProductEvent {
	private Long orderId;
	private List<ResOrderDTO.OrderDTO.OrderLineDTO> orderLineDTO;
}
