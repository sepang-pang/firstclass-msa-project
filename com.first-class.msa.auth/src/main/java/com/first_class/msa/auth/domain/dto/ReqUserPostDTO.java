package com.first_class.msa.auth.domain.dto;

import com.first_class.msa.auth.domain.model.Role;
import com.first_class.msa.auth.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqUserPostDTO {

    private String account;
    private String password;
    private String username;
    private String phone;
    private String role;
    private String slackEmail;

    public static User toEntity(ReqUserPostDTO dto) {
        return User.builder()
                .account(dto.getAccount())
                .password(dto.getPassword())
                .username(dto.getUsername())
                .phone(dto.getPhone())
                .role(Role.valueOf(dto.getRole()))
                .slackEmail(dto.getSlackEmail())
                .build();
    }
}