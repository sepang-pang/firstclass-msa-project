package com.first_class.msa.orders.application.service;

import com.first_class.msa.orders.application.dto.ResOrderPostDTO;
import com.first_class.msa.orders.presentation.request.ReqOrderPostDTO;

public interface OrderService {

	ResOrderPostDTO postBy(Long businessId, Long userId, ReqOrderPostDTO reqOrderPostDTO);
}
