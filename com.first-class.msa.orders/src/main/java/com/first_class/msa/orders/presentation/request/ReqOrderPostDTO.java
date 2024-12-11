package com.first_class.msa.orders.presentation.request;

import java.util.List;

import lombok.Getter;

@Getter
public class ReqOrderPostDTO {
	private String requestInfo;
	private List<ReqOrderLinePostDTO> reqOrderLinePostDTOList;

	@Getter
	public static class ReqOrderLinePostDTO{
		private Long productId;
		private int count;
		private int supplyPrice;
	}
}
