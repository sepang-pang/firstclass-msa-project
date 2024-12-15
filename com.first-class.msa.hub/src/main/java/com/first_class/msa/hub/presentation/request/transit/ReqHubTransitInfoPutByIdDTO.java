package com.first_class.msa.hub.presentation.request.transit;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqHubTransitInfoPutByIdDTO {

    @NotNull(message = "허브 간 이동 정보를 입력해주세요.")
    private HubTransitInfoDTO hubTransitInfoDTO;

    @Getter
    @NoArgsConstructor
    public static class HubTransitInfoDTO {

        @NotNull(message = "출발 허브 정보를 입력해주세요.")
        private Long departureHubId;

        @NotNull(message = "도착 허브 정보를 입력해주세요.")
        private Long arrivalHubId;

    }
}
