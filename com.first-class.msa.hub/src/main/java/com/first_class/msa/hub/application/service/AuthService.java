package com.first_class.msa.hub.application.service;

import com.first_class.msa.hub.application.dto.external.ResRoleGetByUserIdDTO;

public interface AuthService {

    ResRoleGetByUserIdDTO getRoleBy(Long userId);
}
