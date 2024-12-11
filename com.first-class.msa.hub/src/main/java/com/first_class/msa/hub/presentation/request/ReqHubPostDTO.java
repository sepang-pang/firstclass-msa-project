package com.first_class.msa.hub.presentation.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqHubPostDTO {

    @Valid
    @NotNull(message = "허브 정보를 입력해주세요")
    private HubDTO hubDTO;

    @Getter
    @NoArgsConstructor
    public static class HubDTO {

        @NotBlank(message = "허브 이름을 입력해주세요")
        private String name;

        @NotNull(message = "허브 위도를 입력해주세요")
        private double latitude;

        @NotNull(message = "허브 경도를 입력해주세요")
        private double longitude;

        @NotBlank(message = "허브 주소를 입력해주세요")
        private String address;

        @NotBlank(message = "허브 상세 주소를 입력해주세요")
        private String addressDetail;
    }
}
