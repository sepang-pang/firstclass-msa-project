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
    private Role role;
    private String slackEmail;

}