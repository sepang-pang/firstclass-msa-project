package com.first_class.msa.auth.domain.controller.external;

import com.first_class.msa.auth.domain.dto.external.ExternalResRoleGetByIdDTO;
import com.first_class.msa.auth.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExternalUserController {

    private final UserService userService;

    @GetMapping("/external/users/{userId}")
    public ExternalResRoleGetByIdDTO getRoleBy(@PathVariable Long userId) {
        return userService.getRoleBy(userId);
    }
}
