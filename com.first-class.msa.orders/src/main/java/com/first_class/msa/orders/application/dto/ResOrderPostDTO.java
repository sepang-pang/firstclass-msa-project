package com.first_class.msa.orders.application.dto;

import java.util.List;

import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.model.OrderLine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResOrderPostDTO {
	private OrderDTO orderDTO;

	public static ResOrderPostDTO of(Order order){
		return ResOrderPostDTO.builder().orderDTO(OrderDTO.from(order)).build();
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderDTO {

		private Long orderId;
		private List<OrderLineDTO> orderLineList;

		public static OrderDTO from(Order order) {
			return OrderDTO.builder()
				.orderId(order.getId())
				.orderLineList(OrderLineDTO.from(order.getOrderLineList()))
				.build();
		}

		@Getter
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class OrderLineDTO {

			private Long productId;
			private int count;
			private int supplyPrice;

			public static List<OrderLineDTO> from(List<OrderLine> orderLineList) {
				return orderLineList.stream()
					.map(OrderLineDTO::from)
					.toList();
			}

			public static OrderLineDTO from(OrderLine orderLine) {
				return OrderLineDTO.builder()
					.productId(orderLine.getProductId())
					.count(orderLine.getCount().getValue())
					.supplyPrice(orderLine.getSupplyPrice().getValue())
					.build();
			}
		}
	}

}
