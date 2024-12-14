package com.first_class.msa.product.application.dto;

import com.first_class.msa.product.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResProductSearchDTO {

    private ProductPage productPage;

    public static ResProductSearchDTO of(Page<Product> productPage) {
        return ResProductSearchDTO.builder()
                .productPage(ProductPage.from(productPage))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPage {

        private List<ProductDTO> content;
        private PageDetails page;

        public static ProductPage from(Page<Product> productPage) {
            return ProductPage.builder()
                    .content(ProductDTO.from(productPage.getContent()))
                    .page(PageDetails.from(productPage))
                    .build();
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ProductDTO {

            private Long id;
            private Long hubId;
            private Long businessId;
            private String name;
            private int price;
            private int quantity;
            private LocalDateTime createdAt;

            public static List<ProductDTO> from(List<Product> productList) {
                return productList.stream()
                        .map(ProductDTO::from)
                        .toList();
            }

            public static ProductDTO from(Product product) {
                return ProductDTO.builder()
                        .id(product.getId())
                        .hubId(product.getHubId())
                        .businessId(product.getBusinessId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .createdAt(product.getCreatedAt())
                        .build();
            }

        }


        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PageDetails {

            private int size;
            private int number;
            private long totalElements;
            private int totalPages;

            public static PageDetails from(Page<Product> productPage) {
                return PageDetails.builder()
                        .size(productPage.getSize())
                        .number(productPage.getNumber())
                        .totalElements(productPage.getTotalElements())
                        .totalPages(productPage.getTotalPages())
                        .build();
            }
        }
    }
}
