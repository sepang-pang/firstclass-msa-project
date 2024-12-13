package com.first_class.msa.product.application.service;

import com.first_class.msa.product.application.dto.ResRoleGetByUserIdDTO;


public interface AuthService {

    ResRoleGetByUserIdDTO getRoleBy(Long userId);
}
