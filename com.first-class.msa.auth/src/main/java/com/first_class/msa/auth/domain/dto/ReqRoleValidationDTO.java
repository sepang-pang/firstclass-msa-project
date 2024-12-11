package com.first_class.msa.auth.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
public class ReqRoleValidationDTO {
    String role;
    Set<String> roles = new HashSet<>();
}
