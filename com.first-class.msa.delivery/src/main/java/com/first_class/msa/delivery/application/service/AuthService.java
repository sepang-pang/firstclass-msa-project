package com.first_class.msa.delivery.application.service;

import com.first_class.msa.delivery.application.dto.ResRoleGetByIdDTO;

public interface AuthService {
	ResRoleGetByIdDTO getRoleBy(Long userId);
}
