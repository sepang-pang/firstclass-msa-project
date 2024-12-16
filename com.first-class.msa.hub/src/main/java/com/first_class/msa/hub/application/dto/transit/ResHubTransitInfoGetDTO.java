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
        private String departureHubName;
        private Long arrivalHubId;
        private String arrivalHubName;
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
                    .departureHubName(hubTransitInfo.getDepartureHub().getName())
                    .arrivalHubId(hubTransitInfo.getArrivalHub().getId())
                    .arrivalHubName(hubTransitInfo.getArrivalHub().getName())
                    .transitTime(hubTransitInfo.getTransitTime())
                    .distance(hubTransitInfo.getDistance())
                    .build();
        }
    }
}