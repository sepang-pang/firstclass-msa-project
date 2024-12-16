package com.first_class.msa.product.application.dto.external;


import com.first_class.msa.product.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalResProductGetByIdInDTO {

    private Long productId;
    private Long hubId;
    private Integer count;
    private Integer supplyPrice;

    public static ExternalResProductGetByIdInDTO of(Product product) {
        return ExternalResProductGetByIdInDTO.builder()
                .productId(product.getId())
                .hubId(product.getHubId())
                .count(product.getQuantity())
                .supplyPrice(product.getPrice())
                .build();
    }
}
