package com.first_class.msa.orders.application.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqRoleValidationDTO {

	String role;
	Set<String> roles = new HashSet<>();

	public static ReqRoleValidationDTO from(String role) {
		return ReqRoleValidationDTO.builder()
			.role(role)
			.build();
	}

	public static ReqRoleValidationDTO from(Set<String> roles) {
		return ReqRoleValidationDTO.builder()
			.roles(roles)
			.build();
	}
}
