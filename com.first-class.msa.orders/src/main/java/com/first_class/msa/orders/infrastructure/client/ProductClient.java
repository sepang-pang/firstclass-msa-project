package com.first_class.msa.orders.infrastructure.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.first_class.msa.orders.application.dto.ResProductGetDTO;
import com.first_class.msa.orders.application.service.ProductService;

@FeignClient(name = "product-service")
public interface ProductClient extends ProductService {

	@GetMapping("/products")
	List<ResProductGetDTO> getProductList(@RequestParam List<Long> productIdList);
}
