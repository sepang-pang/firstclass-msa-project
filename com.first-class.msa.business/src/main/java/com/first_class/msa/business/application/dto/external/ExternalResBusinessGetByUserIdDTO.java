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
public class ExternalResBusinessGetByUserIdDTO {

    private Long businessId;
    private Long hubId;

    public static ExternalResBusinessGetByUserIdDTO of(Business business) {
        return ExternalResBusinessGetByUserIdDTO.builder()
                .businessId(business.getId())
                .hubId(builder().hubId)
                .build();
    }
}
