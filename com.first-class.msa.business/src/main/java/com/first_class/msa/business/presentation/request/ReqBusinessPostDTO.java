package com.first_class.msa.business.presentation.request;

import com.first_class.msa.business.application.dto.ResBusinessPostDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqBusinessPostDTO {

    @Valid
    @NotNull(message = "업체 정보를 입력해주세요")
    private BusinessDTO businessDTO;

    @Getter
    @NoArgsConstructor
    public static class BusinessDTO {

        @NotNull(message = "업체 정보를 입력해주세요")
        private Long hubId;

        @NotNull(message = "업체 관리자 정보를 입력해주세요")
        private Long managerId;

        @NotBlank(message = "업체 이름을 입력해주세요")
        private String name;

        @NotBlank(message = "업체 타입을 입력해주세요")
        private String type;

        @NotBlank(message = "업체 주소를 입력해주세요")
        private String address;

        private String addressDetail;
    }
}
