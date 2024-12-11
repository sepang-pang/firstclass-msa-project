package com.first_class.msa.orders.application.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.model.OrderLine;
import com.first_class.msa.orders.domain.model.valueobject.Count;
import com.first_class.msa.orders.libs.exception.ApiException;
import com.first_class.msa.orders.libs.message.ErrorMessage;
import com.first_class.msa.orders.presentation.request.ReqOrderPostDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {

	private final ProductService productService;

	@Override
	@Transactional
	public List<OrderLine> createOrderLineList(
		List<ReqOrderPostDTO.ReqOrderLinePostDTO> orderLinePostDTOList,
		Order order
	) {
		List<Long> productIdList
			= orderLinePostDTOList.stream()
			.map(ReqOrderPostDTO.ReqOrderLinePostDTO::getProductId)
			.toList();
		// TODO: 2024-12-11 Product 정보 요청
		// List<ResProductDto> resProductDTO = productService.getProductList(productIdList);
		return resProductDTO.stream()
			.map(resProductDto -> {
				ReqOrderPostDTO.ReqOrderLineDTO matchingRequest = orderLinePostDTOList.stream()
					.filter(reqOrderLineDTO -> reqOrderLineDTO.getProductId().equals(resProductDto.getId()))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_PRODUCT)));

				if (resProductDto.getCount() < matchingRequest.getCount()) {
					throw new IllegalArgumentException(new ApiException(ErrorMessage.INSUFFICIENT_COUNT));
				}

				Count count = new Count(matchingRequest.getCount());
				order.addHubId(resProductDto.getHubId());
				return OrderLine.createOrderLine(order, resProductDto.getId(), count,	resProductDto.getSupplyPrice());
			})
			.toList(); ;
	}

}
