package com.first_class.msa.hub.presentation.request.transit;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqHubTransitInfoPostDTO {

    @Valid
    @NotNull(message = "허브 간 이동 정보를 입력해주세요")
    private ReqHubTransitInfoDTO reqHubTransitInfoDTO;

    @Getter
    @NoArgsConstructor
    public static class ReqHubTransitInfoDTO {

        @NotNull(message = "출발 허브를 입력해주세요")
        private Long departureHubId;

        @NotNull(message = "도착 허브를 입력해주세요")
        private Long arrivalHubId;

    }
}
