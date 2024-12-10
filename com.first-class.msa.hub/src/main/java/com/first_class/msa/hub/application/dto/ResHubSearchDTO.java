package com.first_class.msa.hub.application.dto;

import com.first_class.msa.hub.domain.model.Hub;
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
public class ResHubSearchDTO {

    private HubPage hubPage;

    public static ResHubSearchDTO of(Page<Hub> hubPage) {
        return ResHubSearchDTO.builder()
                .hubPage(HubPage.from(hubPage))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubPage {

        private List<HubDTO> content;
        private PageDetails page;

        public static HubPage from(Page<Hub> hubPage) {
            return HubPage.builder()
                    .content(HubDTO.from(hubPage.getContent()))
                    .page(PageDetails.from(hubPage))
                    .build();
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class HubDTO {

            private Long id;
            private String name;
            private double latitude;
            private double longitude;
            private String address;
            private String addressDetail;

            public static List<HubDTO> from(List<Hub> hubList) {
                return hubList.stream()
                        .map(HubDTO::from)
                        .toList();
            }

            public static HubDTO from(Hub hub) {
                return HubDTO.builder()
                        .id(hub.getId())
                        .name(hub.getName())
                        .latitude(hub.getLatitude())
                        .longitude(hub.getLongitude())
                        .address(hub.getAddress())
                        .addressDetail(hub.getAddressDetail())
                        .build();
            }

        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PageDetails {
            // --
            // FIXME : 페이징 처리에 대한 캐싱 기능이 필요 없다고 판단되면, 이전 양식으로 되돌아 갈 것임.
            // --
            private int size;
            private int number;
            private long totalElements;
            private int totalPages;

            public static PageDetails from(Page<Hub> hubPage) {
                return PageDetails.builder()
                        .size(hubPage.getSize())
                        .number(hubPage.getNumber())
                        .totalElements(hubPage.getTotalElements())
                        .totalPages(hubPage.getTotalPages())
                        .build();
            }
        }
    }
}
