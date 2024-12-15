package com.first_class.msa.hub.presentation.controller;

import com.first_class.msa.hub.application.dto.ResDTO;
import com.first_class.msa.hub.application.dto.transit.ResHubTransitInfoPostDTO;
import com.first_class.msa.hub.application.service.HubTransitInfoService;
import com.first_class.msa.hub.presentation.request.transit.ReqHubTransitInfoPostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hubs")
public class HubTransitInfoController {

    private final HubTransitInfoService hubTransitInfoService;

    @PostMapping("/hub-transit-infos")
    public ResponseEntity<ResDTO<ResHubTransitInfoPostDTO>> postBy(@RequestHeader("X-User-Id") Long userId,
                                                                   @RequestHeader("X-User-Account") String account,
                                                                   @RequestBody ReqHubTransitInfoPostDTO dto) {

        return new ResponseEntity<>(
                ResDTO.<ResHubTransitInfoPostDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 생성에 성공하였습니다.")
                        .data(hubTransitInfoService.postBy(userId, account, dto))
                        .build(),
                HttpStatus.OK
        );
    }
}
