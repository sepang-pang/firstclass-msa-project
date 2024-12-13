package com.first_class.msa.business.application.dto.external;

import com.first_class.msa.business.domain.model.Business;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalResBusinessGetByIdDTO {

    private Long businessId;
    private Long managerId;

    public static ExternalResBusinessGetByIdDTO of(Business business) {
        return ExternalResBusinessGetByIdDTO.builder()
                .businessId(business.getId())
                .managerId(business.getManagerId())
                .build();
    }
}
