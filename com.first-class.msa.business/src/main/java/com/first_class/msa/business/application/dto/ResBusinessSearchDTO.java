package com.first_class.msa.business.application.dto;

import com.first_class.msa.business.domain.model.Business;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResBusinessSearchDTO {

    private BusinessPage businessPage;

    public static ResBusinessSearchDTO of(Page<Business> businessPage) {
        return ResBusinessSearchDTO.builder()
                .businessPage(BusinessPage.from(businessPage))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessPage {

        private List<BusinessDTO> content;
        private PageDetails page;

        public static BusinessPage from(Page<Business> businessPage) {
            return BusinessPage.builder()
                    .content(BusinessDTO.from(businessPage.getContent()))
                    .page(PageDetails.from(businessPage))
                    .build();
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class BusinessDTO {

            private Long hubId;
            private String name;
            private String type;
            private String address;
            private String addressDetail;

            public static List<BusinessDTO> from(List<Business> businessList) {
                return businessList.stream()
                        .map(BusinessDTO::from)
                        .toList();
            }

            public static BusinessDTO from(Business business) {
                return BusinessDTO.builder()
                        .hubId(business.getHubId())
                        .name(business.getName())
                        .type(business.getType().getLabel())
                        .address(business.getAddress())
                        .addressDetail(business.getAddressDetail())
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

            public static PageDetails from(Page<Business> businessPage) {
                return PageDetails.builder()
                        .size(businessPage.getSize())
                        .number(businessPage.getNumber())
                        .totalElements(businessPage.getTotalElements())
                        .totalPages(businessPage.getTotalPages())
                        .build();
            }
        }
    }
}
