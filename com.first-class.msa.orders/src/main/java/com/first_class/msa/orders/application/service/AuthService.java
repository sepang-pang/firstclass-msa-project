package com.first_class.msa.orders.application.service;

import org.springframework.web.bind.annotation.PathVariable;

import com.first_class.msa.orders.application.dto.ResRoleGetByIdDTO;

public interface AuthService {

	ResRoleGetByIdDTO getRoleBy(@PathVariable Long userId);
}
