package com.first_class.msa.hub.presentation.controller;

import com.first_class.msa.hub.application.global.success.SuccessResponseDTO;
import com.first_class.msa.hub.application.dto.transit.ResHubTransitInfoGetDTO;
import com.first_class.msa.hub.application.dto.transit.ResHubTransitInfoPostDTO;
import com.first_class.msa.hub.application.service.HubTransitInfoService;
import com.first_class.msa.hub.presentation.request.transit.ReqHubTransitInfoPostDTO;
import com.first_class.msa.hub.presentation.request.transit.ReqHubTransitInfoPutByIdDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hubs")
public class HubTransitInfoController {

    private final HubTransitInfoService hubTransitInfoService;

    @PostMapping("/hub-transit-infos")
    public ResponseEntity<SuccessResponseDTO<ResHubTransitInfoPostDTO>> postBy(@RequestHeader("X-User-Id") Long userId,
                                                                               @RequestHeader("X-User-Account") String account,
                                                                               @Valid @RequestBody ReqHubTransitInfoPostDTO dto) {

        return new ResponseEntity<>(
                SuccessResponseDTO.<ResHubTransitInfoPostDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 생성에 성공하였습니다.")
                        .data(hubTransitInfoService.postBy(userId, account, dto))
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/hub-transit-infos")
    public ResponseEntity<SuccessResponseDTO<ResHubTransitInfoGetDTO>> getBy(@RequestParam(name = "departureHubId") Long departureHubId,
                                                                             @RequestParam(name = "arrivalHubId") Long arrivalHubId) {

        return new ResponseEntity<>(
                SuccessResponseDTO.<ResHubTransitInfoGetDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 이동 정보 조회에 성공하였습니다.")
                        .data(hubTransitInfoService.getBy(departureHubId, arrivalHubId))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/hub-transit-infos/{hubTransitInfoId}")
    public ResponseEntity<SuccessResponseDTO<Objects>> putBy(@RequestHeader("X-User-Id") Long userId,
                                                             @RequestHeader("X-User-Account") String account,
                                                             @Valid @RequestBody ReqHubTransitInfoPutByIdDTO dto,
                                                             @PathVariable(name = "hubTransitInfoId") Long hubTransitInfoId) {


        hubTransitInfoService.putBy(userId, account, hubTransitInfoId, dto);

        return new ResponseEntity<>(
                SuccessResponseDTO.<Objects>builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 이동 정보 수정에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/hub-transit-infos/{hubTransitInfoId}")
    public ResponseEntity<SuccessResponseDTO<Objects>> deleteBy(@RequestHeader("X-User-Id") Long userId,
                                                                @RequestHeader("X-User-Account") String account,
                                                                @PathVariable(name = "hubTransitInfoId") Long hubTransitInfoId) {

        hubTransitInfoService.deleteBy(userId, account, hubTransitInfoId);

        return new ResponseEntity<>(
                SuccessResponseDTO.<Objects>builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 이동 정보 삭제에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }
}
