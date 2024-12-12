package com.first_class.msa.orders.application.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.first_class.msa.orders.application.dto.ReqRoleValidationDTO;

public interface AuthService {

	boolean checkBy(Long userId,  ReqRoleValidationDTO dto);
}
