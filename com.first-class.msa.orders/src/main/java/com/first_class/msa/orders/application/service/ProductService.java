package com.first_class.msa.orders.application.service;

import java.util.List;

import com.first_class.msa.orders.application.dto.ResProductGetDTO;

public interface ProductService {

	List<ResProductGetDTO> checkProductListBy(List<Long> productIdList);
}
