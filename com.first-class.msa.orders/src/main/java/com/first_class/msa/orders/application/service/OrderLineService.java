package com.first_class.msa.orders.application.service;

import java.util.List;

import com.first_class.msa.orders.domain.model.Order;
import com.first_class.msa.orders.domain.model.OrderLine;
import com.first_class.msa.orders.presentation.request.ReqOrderPostDTO;

public interface OrderLineService {

	List<OrderLine> createOrderLineList(List<ReqOrderPostDTO.ReqOrderLinePostDTO> orderLinePostDTOList, Order order);
}
