package com.first_class.msa.hub.application.dto;

import com.first_class.msa.hub.domain.model.Hub;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResHubSearchDTO {

    private HubPage hubPage;

    public static ResHubSearchDTO of(Page<Hub> hubPage) {
        return ResHubSearchDTO.builder()
                .hubPage(new HubPage(hubPage))
                .build();
    }

    public static class HubPage extends PagedModel<HubPage.HubDTO> {

        private HubPage(Page<Hub> hubPage) {
            super(
                    new PageImpl<>(
                            HubDTO.from(hubPage.getContent()),
                            hubPage.getPageable(),
                            hubPage.getTotalElements()
                    )
            );
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
    }
}
