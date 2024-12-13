package com.first_class.msa.business.presentation.controller;

import com.first_class.msa.business.application.dto.ResBusinessPostDTO;
import com.first_class.msa.business.application.dto.ResBusinessSearchDTO;
import com.first_class.msa.business.application.dto.SuccessResponseDTO;
import com.first_class.msa.business.application.service.BusinessService;
import com.first_class.msa.business.presentation.request.ReqBusinessPostDTO;
import com.first_class.msa.business.presentation.request.ReqBusinessPutByIdDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

        return new ResponseEntity<>(
                SuccessResponseDTO.<ResBusinessPostDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("업체 생성에 성공하였습니다.")
                        .data(businessService.postBy(userId, account, dto))
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/business")
    public ResponseEntity<SuccessResponseDTO<ResBusinessSearchDTO>> searchBy(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                                             @RequestParam(name = "name", required = false) String name,
                                                                             @RequestParam(name = "address", required = false) String address,
                                                                             @RequestParam(name = "type", required = false) String type,
                                                                             @RequestParam(name = "sort", required = false) String sort) {

        return new ResponseEntity<>(
                SuccessResponseDTO.<ResBusinessSearchDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("업체 검색에 성공하였습니다.")
                        .data(businessService.searchBy(pageable, name, address, type, sort))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/business/{businessId}")
    public ResponseEntity<SuccessResponseDTO<Object>> putBy(@RequestHeader("X-User-Id") Long userId,
                                                            @RequestHeader("X-User-Account") String account,
                                                            @PathVariable Long businessId,
                                                            @Valid @RequestBody ReqBusinessPutByIdDTO dto) {

        businessService.putBy(userId, account, businessId, dto);

        return new ResponseEntity<>(
                SuccessResponseDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("업체 수정에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/business/{businessId}")
    public ResponseEntity<SuccessResponseDTO<Object>> deleteBy(@RequestHeader("X-User-Id") Long userId,
                                                               @RequestHeader("X-User-Account") String account,
                                                               @PathVariable Long businessId) {

        businessService.deleteBy(userId, account, businessId);

        return new ResponseEntity<>(
                SuccessResponseDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("업체 삭제에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }
}
