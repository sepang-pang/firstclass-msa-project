package com.first_class.msa.message.infrastructure.client;

import com.first_class.msa.message.application.dto.ResProductGetDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products")
    List<ResProductGetDTO> getAllProductBy(@RequestParam List<Long> productIdList);
}
