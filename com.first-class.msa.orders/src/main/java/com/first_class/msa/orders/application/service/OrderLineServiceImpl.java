package com.first_class.msa.orders.application.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.model.OrderLine;
import com.first_class.msa.orders.domain.model.valueobject.Count;
import com.first_class.msa.orders.infrastructure.messaging.OrderEventPublisher;
import com.first_class.msa.orders.libs.exception.ApiException;
import com.first_class.msa.orders.libs.message.ErrorMessage;
import com.first_class.msa.orders.presentation.request.ReqOrderPostDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {

	private final OrderEventPublisher orderEventPublisher;
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
		// List<ResProductDto> resProductDTO = productService.getProductList(productIds);
		return resProductDTO.stream()
			.map(resProductDto -> {
				ReqOrderPostDTO.ReqOrderLinePostDTO matchingRequest = resProductDto.stream()
					.filter(orderProductRequest -> orderProductRequest.getProductId().equals(resProductDto.getId()))
					.findFirst()
					.orElseThrow(() -> new ApiException(ErrorMessage.NOT_FOUND_PRODUCT));

				Count count = new Count(matchingRequest.getCount());
				return OrderLine.createOrderLine(order, resProductDto.getId(), count, resProductDto.getSupplyPrice());
			})
			.toList();;
	}



}
