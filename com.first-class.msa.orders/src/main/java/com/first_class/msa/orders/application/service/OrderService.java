package com.first_class.msa.orders.application.service;

import org.springframework.data.domain.Pageable;

import com.first_class.msa.orders.application.dto.ResOrderDTO;
import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;
import com.first_class.msa.orders.presentation.request.ReqOrderPostDTO;

public interface OrderService {

	ResOrderDTO postBy(Long businessId, Long userId, ReqOrderPostDTO reqOrderPostDTO);

	ResOrderSearchDTO getAllOrderBy(Long userId, Pageable pageable);

	ResOrderDTO getOrderDetailBy(Long userId, Long orderId);

	void deleteBy(Long userId, Long orderId);



}
