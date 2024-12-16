package com.first_class.msa.product.presentation.controller.external;

import com.first_class.msa.product.application.dto.external.ExternalResProductGetByIdInDTO;
import com.first_class.msa.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external")
public class ExternalProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<ExternalResProductGetByIdInDTO> getBy(@RequestParam List<Long> productIdList) {
        return productService.getBy(productIdList);
    }
}
