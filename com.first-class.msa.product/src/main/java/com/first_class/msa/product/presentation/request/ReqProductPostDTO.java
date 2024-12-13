package com.first_class.msa.product.presentation.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqProductPostDTO {

    @Valid
    @NotNull(message = "상품 정보를 입력해주세요")
    private ProductDTO productDTO;


    @Getter
    @NoArgsConstructor
    public static class ProductDTO {

        @NotNull(message = "업체 정보를 입력해주세요")
        private Long businessId;

        @NotNull(message = "허브 정보를 입력해주세요")
        private Long hubId;

        @NotBlank(message = "상품 이름을 입력해주세요")
        private String name;

        @NotNull(message = "가격을 입력해주세요")
        @Min(value = 100, message = "가격은 최소 100원 이상이어야 합니다")
        private int price;

        @NotNull(message = "재고를 입력해주세요")
        @Min(value = 10, message = "재고는 최소 10개 이상이어야 합니다")
        private int quantity;

    }
}
