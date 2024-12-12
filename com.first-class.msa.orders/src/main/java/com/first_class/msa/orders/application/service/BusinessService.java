package com.first_class.msa.orders.application.service;

import com.first_class.msa.orders.application.dto.ResBusinessDTO;

public interface BusinessService {

	ResBusinessDTO checkBusinessBy(Long businessId);

	ResBusinessDTO checkBusinessUserBy(Long userId);

}
