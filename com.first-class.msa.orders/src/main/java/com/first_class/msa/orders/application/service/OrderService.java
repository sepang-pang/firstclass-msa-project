package com.first_class.msa.orders.application.service;

import com.first_class.msa.orders.application.dto.ResOrderPostDTO;
import com.first_class.msa.orders.presentation.request.ReqOrderDTO;

public interface OrderService {

	ResOrderPostDTO postBy(Long businessId, Long userId, ReqOrderDTO reqOrderPostDTO);

	void putBy(Long businessId, Long userId, ReqOrderDTO reqOrderPostDTO);
}
