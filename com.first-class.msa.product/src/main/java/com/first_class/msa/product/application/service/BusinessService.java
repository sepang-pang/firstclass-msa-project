package com.first_class.msa.product.application.service;

import com.first_class.msa.product.application.dto.external.ExternalResBusinessGetByIdDTO;
import org.springframework.web.bind.annotation.PathVariable;

public interface BusinessService {

    ExternalResBusinessGetByIdDTO getBy(@PathVariable("businessId") Long businessId);

    boolean existsBy(@PathVariable("businessId") Long businessId);
}
