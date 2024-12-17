package com.first_class.msa.message.application.service;

import com.first_class.msa.message.application.dto.ResProductGetDTO;

import java.util.List;

public interface ProductService {
    List<ResProductGetDTO> getAllProductBy(List<Long> productIdList);
}
