package com.first_class.msa.message.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResHubTransitInfoGetDTO {

    private List<HubTransitInfoDTO> hubTransitInfoDTOList;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubTransitInfoDTO {

        private String departureHubName;
        private String arrivalHubName;
        private Long transitTime;
        private double distance;
    }
}