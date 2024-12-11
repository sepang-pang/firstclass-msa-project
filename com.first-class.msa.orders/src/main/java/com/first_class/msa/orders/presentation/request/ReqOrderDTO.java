package com.first_class.msa.orders.presentation.request;

import java.util.List;

import lombok.Getter;

@Getter
public class ReqOrderDTO {
	private String requestInfo;
	private List<ReqOrderLineDTO> reqOrderLinePostDTOList;

	@Getter
	public static class ReqOrderLineDTO{
		private Long productId;
		private int count;
		private int supplyPrice;
	}
}
