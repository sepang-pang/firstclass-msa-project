package com.first_class.msa.product.presentation.controller;

import com.first_class.msa.product.application.dto.ResProductPostDTO;
import com.first_class.msa.product.application.dto.SuccessResponseDTO;
import com.first_class.msa.product.application.service.ProductService;
import com.first_class.msa.product.presentation.request.ReqProductPostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<SuccessResponseDTO<ResProductPostDTO>> postBy (@RequestHeader("X-User-Id") Long userId,
                                                                         @RequestHeader("X-User-Account") String account,
                                                                         @RequestBody ReqProductPostDTO dto) {


        return new ResponseEntity<>(
                SuccessResponseDTO.<ResProductPostDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("상품 생성에 성공하였습니다.")
                        .data(productService.postBy(userId, account, dto))
                        .build(),
                HttpStatus.OK
        );
    }
}
