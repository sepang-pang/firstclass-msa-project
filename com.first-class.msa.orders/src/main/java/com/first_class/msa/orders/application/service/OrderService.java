package com.first_class.msa.orders.application.service;

import org.springframework.data.domain.Pageable;

import com.first_class.msa.orders.application.dto.ResOrderPostDTO;
import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;
import com.first_class.msa.orders.application.dto.ResOrderSearchDetailDTO;
import com.first_class.msa.orders.presentation.request.ReqOrderPostDTO;

public interface OrderService {

	ResOrderPostDTO postBy(Long businessId, Long userId, ReqOrderPostDTO reqOrderPostDTO);

	ResOrderSearchDTO getAllOrderBy(Long userId, Pageable pageable);

	ResOrderSearchDetailDTO getOrderDetailBy(Long userId, Long orderId);



}
