package com.first_class.msa.product.infrastructure.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreateProductEvent {
	private Long orderId;
	private List<OrderLineDTO> orderLineDTO;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OrderLineDTO {
		private Long productId;
		private int count;
		private int supplyPrice;
	}

}
