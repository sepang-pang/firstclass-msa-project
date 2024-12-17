package com.first_class.msa.message.infrastructure.client;

import com.first_class.msa.message.application.dto.ResUserGetByIdDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service")
public interface UserClient {

    @GetMapping("/auth/users")
    ResponseEntity<ResUserGetByIdDTO>  getUser(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String slackId,
            @RequestParam(required = false) String username);
}

