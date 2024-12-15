package com.first_class.msa.hub.application.dto.transit;

import com.first_class.msa.hub.domain.model.HubTransitInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResHubTransitInfoGetDTO {

    private List<HubTransitInfoDTO> hubTransitInfoDTOList;

    public static ResHubTransitInfoGetDTO of(List<HubTransitInfo> hubTransitInfoList) {
        return ResHubTransitInfoGetDTO.builder()
                .hubTransitInfoDTOList(HubTransitInfoDTO.from(hubTransitInfoList))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubTransitInfoDTO {

        private Long departureHubId;
        private Long arrivalHubId;
        private Long transitTime;
        private double distance;

        public static List<HubTransitInfoDTO> from(List<HubTransitInfo> hubTransitInfoList) {
            return hubTransitInfoList.stream()
                    .map(HubTransitInfoDTO::from)
                    .toList();
        }

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