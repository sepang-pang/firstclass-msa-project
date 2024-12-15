package com.first_class.msa.product.application.dto;

import com.first_class.msa.product.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResProductPostDTO {

    private ProductDTO productDTO;

    public static ResProductPostDTO of(Product product) {
        return ResProductPostDTO.builder()
                .productDTO(ProductDTO.from(product))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDTO {

        private Long businessId;
        private Long hubId;
        private String name;
        private int price;
        private int quantity;

        public static ProductDTO from(Product product) {
            return ProductDTO.builder()
                    .businessId(product.getBusinessId())
                    .hubId(product.getHubId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .build();
        }
    }
}
