package com.first_class.msa.message.application.service;

import com.first_class.msa.message.application.dto.ResOrderGetDTO;

public interface OrderService {

    ResOrderGetDTO getOrder(Long orderId);
}
