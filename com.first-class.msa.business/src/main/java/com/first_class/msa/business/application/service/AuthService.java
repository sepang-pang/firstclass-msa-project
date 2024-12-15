package com.first_class.msa.business.application.service;

import com.first_class.msa.business.application.dto.ResRoleGetByUserIdDTO;
import org.springframework.web.bind.annotation.PathVariable;

public interface AuthService {

    ResRoleGetByUserIdDTO getRoleBy(@PathVariable(name = "userId") Long userId);
}
