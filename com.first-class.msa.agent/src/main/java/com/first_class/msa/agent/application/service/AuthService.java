package com.first_class.msa.agent.application.service;

import com.first_class.msa.agent.application.dto.ResRoleGetByIdDTO;

public interface AuthService {
	ResRoleGetByIdDTO getRoleBy(Long userId);
}
