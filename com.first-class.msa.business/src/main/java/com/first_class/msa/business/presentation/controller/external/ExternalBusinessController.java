package com.first_class.msa.business.presentation.controller.external;

import com.first_class.msa.business.application.dto.external.ExternalResBusinessGetByIdDTO;
import com.first_class.msa.business.application.dto.external.ExternalResBusinessGetByUserIdDTO;
import com.first_class.msa.business.application.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExternalBusinessController {

    private final BusinessService businessService;

    @GetMapping("/external/business/{businessId}/exists")
    public boolean existsBy(@PathVariable("businessId") Long businessId) {
        return businessService.existsBy(businessId);
    }

    @GetMapping("/external/business/{businessId}")
    public ExternalResBusinessGetByIdDTO getBy(@PathVariable("businessId") Long businessId) {
        return businessService.getBy(businessId);
    }

    @GetMapping("/external/business")
    public ExternalResBusinessGetByUserIdDTO getByUserId(@RequestParam Long userId) {
        return businessService.getByUserId(userId);
    }
}
