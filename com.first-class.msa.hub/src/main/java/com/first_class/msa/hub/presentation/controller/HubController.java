package com.first_class.msa.hub.presentation.controller;

import com.first_class.msa.hub.application.dto.ReqRoleValidationDTO;
import com.first_class.msa.hub.application.dto.ResDTO;
import com.first_class.msa.hub.application.dto.ResHubPostDTO;
import com.first_class.msa.hub.application.dto.ResHubSearchDTO;
import com.first_class.msa.hub.application.service.HubService;
import com.first_class.msa.hub.domain.model.Hub;
import com.first_class.msa.hub.infrastructure.client.AuthClient;
import com.first_class.msa.hub.presentation.request.ReqHubPostDTO;
import com.first_class.msa.hub.presentation.request.ReqHubPutByIdDTO;
import com.querydsl.core.types.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;
    private final AuthClient authClient;

    @PostMapping("/hubs")
    public ResponseEntity<ResDTO<ResHubPostDTO>> postBy(@RequestHeader("X-User-Id") Long userId,
                                                        @RequestHeader("X-User-Account") String account,
                                                        @Valid @RequestBody ReqHubPostDTO req) {

        if (!authClient.checkBy(userId, ReqRoleValidationDTO.from("MANAGER"))) {
            throw new IllegalArgumentException("관리자가 아닙니다. 접근 권한이 없습니다.");
        }

        if (!authClient.checkBy(req.getHubDTO().getManagerId(), ReqRoleValidationDTO.from("HUB_MANAGER"))) {
            throw new IllegalArgumentException("허브 관리자가 아닙니다.");
        }

        return new ResponseEntity<>(
                ResDTO.<ResHubPostDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 생성에 성공하였습니다.")
                        .data(hubService.postBy(account, req))
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/hubs")
    public ResponseEntity<ResDTO<ResHubSearchDTO>> searchBy(@QuerydslPredicate(root = Hub.class) Predicate predicate,
                                                            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {


        return new ResponseEntity<>(
                ResDTO.<ResHubSearchDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 검색에 성공하였습니다.")
                        .data(hubService.searchBy(predicate, pageable))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/hubs/{hubId}")
    public ResponseEntity<ResDTO<Object>> putBy(@RequestHeader("X-User-Id") Long userId,
                                                @RequestHeader("X-User-Account") String account,
                                                @PathVariable(name = "hubId") Long hubId,
                                                @Valid @RequestBody ReqHubPutByIdDTO dto) {

        // --
        // TODO : MASTER 권한 검증
        // --
        // --
        // TODO : DataIntegrityViolationException 예외처리
        // --

        hubService.putBy(account, hubId, dto);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 수정에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/hubs/{hubId}")
    public ResponseEntity<ResDTO<Object>> deleteBy(@RequestHeader("X-User-Id") Long userId,
                                                   @RequestHeader("X-User-Account") String account,
                                                   @PathVariable(name = "hubId") Long hubId) {

        // --
        // TODO : MASTER 권한 검증
        // --

        hubService.deleteBy(account, hubId);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 삭제에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }


    @GetMapping("/hubs/{hubId}/exists")
    public boolean existsBy(@PathVariable(name = "hubId") Long hubId) {
        return hubService.existsBy(hubId);
    }

    @GetMapping("/hub/by-user/{userId}")
    public Long getHubIdBy(@PathVariable Long userId) {
        return hubService.getHubIdBy(userId);
    }

}
