package com.first_class.msa.business.presentation.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqBusinessPutByIdDTO {

    @Valid
    @NotNull(message = "업체 정보를 입력해주세요")
    private BusinessDTO businessDTO;

    @Getter
    @NoArgsConstructor
    public static class BusinessDTO {

        @NotNull(message = "업체 관리자 정보를 입력해주세요")
        private Long userId;

        @NotNull(message = "관리 허브 정보를 입력해주세요")
        private Long hubId;

        @NotBlank(message = "업체 이름을 입력해주세요")
        private String name;

        @NotBlank(message = "업체 타입을 입력해주세요")
        private String type;

        @NotBlank(message = "업체 주소를 입력해주세요")
        private String address;

        private String addressDetail;

    }
}
