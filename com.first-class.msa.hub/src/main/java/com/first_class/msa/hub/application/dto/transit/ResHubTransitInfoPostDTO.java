package com.first_class.msa.hub.application.dto.transit;

import com.first_class.msa.hub.domain.model.HubTransitInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResHubTransitInfoPostDTO {

    private HubTransitInfoDTO  hubTransitInfoDTO;

    public static ResHubTransitInfoPostDTO of(HubTransitInfo hubTransitInfo) {
        return ResHubTransitInfoPostDTO.builder()
                .hubTransitInfoDTO(HubTransitInfoDTO.from(hubTransitInfo))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubTransitInfoDTO {

        private Long departureHubId;
        private Long arrivalHubId;
        private Duration transitTime;
        private double distance;

        public static HubTransitInfoDTO from(HubTransitInfo hubTransitInfo) {
            return HubTransitInfoDTO.builder()
                    .departureHubId(hubTransitInfo.getDepartureHub().getId())
                    .arrivalHubId(hubTransitInfo.getArrivalHub().getId())
                    .transitTime(hubTransitInfo.getTransitTime())
                    .distance(hubTransitInfo.getDistance())
                    .build();

        }
    }
}
