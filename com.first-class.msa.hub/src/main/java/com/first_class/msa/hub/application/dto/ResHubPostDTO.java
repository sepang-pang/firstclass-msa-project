package com.first_class.msa.hub.application.dto;

import com.first_class.msa.hub.domain.model.Hub;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResHubPostDTO {

    private HubDTO hubDTO;

    public static ResHubPostDTO of(Hub hub) {
        return ResHubPostDTO.builder()
                .hubDTO(HubDTO.from(hub))
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
