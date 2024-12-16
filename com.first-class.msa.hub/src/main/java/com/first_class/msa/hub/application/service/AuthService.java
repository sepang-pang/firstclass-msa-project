package com.first_class.msa.hub.application.service;

import com.first_class.msa.hub.application.dto.external.ExternalResRoleGetByUserIdDTO;

public interface AuthService {

    ExternalResRoleGetByUserIdDTO getRoleBy(Long userId);
}
