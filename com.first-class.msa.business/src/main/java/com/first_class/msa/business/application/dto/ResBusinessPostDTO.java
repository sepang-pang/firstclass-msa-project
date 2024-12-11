package com.first_class.msa.business.application.dto;


import com.first_class.msa.business.domain.model.Business;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResBusinessPostDTO {

    private BusinessDTO businessDTO;

    public static ResBusinessPostDTO of(Business business) {
        return ResBusinessPostDTO.builder()
                .businessDTO(BusinessDTO.from(business))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessDTO {

        private Long id;
        private Long userId;
        private Long hubId;
        private String name;
        private String type;
        private String address;
        private String addressDetail;

        public static BusinessDTO from(Business business) {
            return BusinessDTO.builder()
                    .id(business.getId())
                    .userId(business.getUserId())
                    .hubId(business.getHubId())
                    .name(business.getName())
                    .type(business.getType().getLabel())
                    .address(business.getAddress())
                    .addressDetail(business.getAddressDetail())
                    .build();
        }
    }
}
