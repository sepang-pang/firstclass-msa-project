package com.first_class.msa.hub.presentation.controller;

import com.first_class.msa.hub.application.dto.ResDTO;
import com.first_class.msa.hub.application.dto.ResHubPostDTO;
import com.first_class.msa.hub.application.service.HubService;
import com.first_class.msa.hub.presentation.request.ReqHubPostDTO;
import com.first_class.msa.hub.presentation.request.ReqHubPutByIdDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    // --
    // FIXME : 임시로 @RequestParam 을 통해 userId 를 받아오고 있습니다. 추후 gateway 구현 정도에 따라 변경될 부분입니다.
    // --
    @PostMapping("/hubs")
    public ResponseEntity<ResDTO<ResHubPostDTO>> postBy(@RequestParam(name = "userId") Long userId,
                                                        @Valid @RequestBody ReqHubPostDTO req) {

        // --
        // TODO : MASTER 권한 검증
        // --

        return new ResponseEntity<>(
                ResDTO.<ResHubPostDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 생성에 성공하였습니다.")
                        .data(hubService.postBy(userId, req))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/hubs/{hubId}")
    public ResponseEntity<ResDTO<Object>> putBy(@RequestParam(name = "userId") Long userId,
                                                @PathVariable(name = "hubId") Long hubId,
                                                @Valid @RequestBody ReqHubPutByIdDTO dto) {

        // --
        // TODO : MASTER 권한 검증
        // --
        // --
        // TODO : DataIntegrityViolationException 예외처리
        // --

        hubService.putBy(userId, hubId, dto);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 수정에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/hubs/{hubId}")
    public ResponseEntity<ResDTO<Object>> deleteBy(@RequestParam(name = "userId") Long userId,
                                                   @PathVariable(name = "hubId") Long hubId) {

        // --
        // TODO : MASTER 권한 검증
        // --

        hubService.deleteBy(userId, hubId);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 삭제에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

}
