package com.first_class.msa.product.presentation.controller;

import com.first_class.msa.product.application.dto.ResProductPostDTO;
import com.first_class.msa.product.application.dto.ResProductSearchDTO;
import com.first_class.msa.product.application.global.success.SuccessResponseDTO;
import com.first_class.msa.product.application.service.ProductService;
import com.first_class.msa.product.presentation.request.ReqProductPostDTO;
import com.first_class.msa.product.presentation.request.ReqProductPutByIdDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<SuccessResponseDTO<ResProductPostDTO>> postBy(@RequestHeader("X-User-Id") Long userId,
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

    @GetMapping("/products")
    public ResponseEntity<SuccessResponseDTO<ResProductSearchDTO>> getBy(@RequestHeader("X-User-Id") Long userId,
                                                                         @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                                         @RequestParam(name = "name", required = false) String name,
                                                                         @RequestParam(name = "minPrice", required = false) Integer minPrice,
                                                                         @RequestParam(name = "maxPrice", required = false) Integer maxPrice,
                                                                         @RequestParam(name = "sort", required = false) String sort) {


        return new ResponseEntity<>(
                SuccessResponseDTO.<ResProductSearchDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("상품 검색에 성공하였습니다.")
                        .data(productService.searchBy(userId, pageable, name, minPrice, maxPrice, sort))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<SuccessResponseDTO<Objects>> putBy(@RequestHeader("X-User-Id") Long userId,
                                                             @RequestHeader("X-User-Account") String account,
                                                             @PathVariable(name = "productId") Long productId,
                                                             @RequestBody ReqProductPutByIdDTO dto) {

        productService.putBy(userId, account, productId, dto);

        return new ResponseEntity<>(
                SuccessResponseDTO.<Objects>builder()
                        .code(HttpStatus.OK.value())
                        .message("상품 수정에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<SuccessResponseDTO<Objects>> deleteBy(@RequestHeader("X-User-Id") Long userId,
                                                                @RequestHeader("X-User-Account") String account,
                                                                @PathVariable(name = "productId") Long productId) {

        productService.deleteBy(userId, account, productId);

        return new ResponseEntity<>(
                SuccessResponseDTO.<Objects>builder()
                        .code(HttpStatus.OK.value())
                        .message("상품 삭제에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }
}
