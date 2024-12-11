package com.first_class.msa.orders.application.dto;

import org.springframework.data.domain.Page;

import com.first_class.msa.orders.domain.model.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResOrderSearchDTO {

	private Page<OrderPage> orderPage;

	public static ResOrderSearchDTO of(Page<ResOrderSearchDTO.OrderPage> orderPage) {
		return ResOrderSearchDTO.builder()
			.orderPage(orderPage)
			.build();
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderPage {

		private Long orderId;
		private String requestInfo;
		private Integer orderTotalPrice;

		public static OrderPage from(Order order) {
			return OrderPage.builder()
				.orderId(order.getId())
				.requestInfo(order.getRequestInfo().getValue())
				.orderTotalPrice(order.getOrderTotalPrice().getValue())
				.build();
		}
	}
}
