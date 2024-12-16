package com.first_class.msa.delivery.application.service;

import com.first_class.msa.delivery.application.dto.ExternalResBusinessGetByIdDTO;

public interface BusinessService {
	ExternalResBusinessGetByIdDTO getBy(Long businessId);

}
