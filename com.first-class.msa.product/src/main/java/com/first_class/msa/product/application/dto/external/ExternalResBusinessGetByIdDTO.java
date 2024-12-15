package com.first_class.msa.product.application.dto.external;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExternalResBusinessGetByIdDTO {
    private Long businessId;
    private Long managerId;
}
