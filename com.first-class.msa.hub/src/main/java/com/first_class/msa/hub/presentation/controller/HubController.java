package com.first_class.msa.hub.presentation.controller;

import com.first_class.msa.hub.application.dto.external.ResRoleGetByUserIdDTO;
import com.first_class.msa.hub.application.dto.ResDTO;
import com.first_class.msa.hub.application.dto.ResHubPostDTO;
import com.first_class.msa.hub.application.dto.ResHubSearchDTO;
import com.first_class.msa.hub.application.service.AuthService;
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

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;
    private final AuthService authService;

    @PostMapping("/hubs")
    public ResponseEntity<ResDTO<ResHubPostDTO>> postBy(@RequestHeader("X-User-Id") Long userId,
                                                        @RequestHeader("X-User-Account") String account,
                                                        @Valid @RequestBody ReqHubPostDTO req) {

        validateUserRole(userId);


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

        validateUserRole(userId);
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

        validateUserRole(userId);

        hubService.deleteBy(account, hubId);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("허브 삭제에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    private void validateUserRole(Long userId) {
        if(!Objects.equals("MASTER", authService.getRoleBy(userId).getRole())) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
    }
}
