package com.first_class.msa.business.presentation.controller;

import com.first_class.msa.business.application.dto.ResBusinessPostDTO;
import com.first_class.msa.business.application.dto.SuccessResponseDTO;
import com.first_class.msa.business.application.service.BusinessService;
import com.first_class.msa.business.presentation.request.ReqBusinessPostDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping("/business")
    public ResponseEntity<SuccessResponseDTO<ResBusinessPostDTO>> postBy(@RequestHeader("X-User-Id") Long userId,
                                                                         @RequestHeader("X-User-Account") String account,
                                                                         @Valid @RequestBody ReqBusinessPostDTO dto) {

        // --
        // TODO : 권한체크 ( 마스터, 허브 매니저, 업체 매니저 )
        // --

        return new ResponseEntity<>(
                SuccessResponseDTO.<ResBusinessPostDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("업체 생성에 성공하였습니다.")
                        .data(businessService.postBy(userId, account, dto))
                        .build(),
                HttpStatus.OK
        );
    }
}
