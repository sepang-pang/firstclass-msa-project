package com.first_class.msa.business.presentation.controller.external;

import com.first_class.msa.business.application.dto.external.ExternalResBusinessGetByIdDTO;
import com.first_class.msa.business.application.dto.external.ExternalResBusinessGetByUserIdDTO;
import com.first_class.msa.business.application.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external")
public class ExternalBusinessController {

    private final BusinessService businessService;

    @GetMapping("/business/{businessId}/exists")
    public boolean existsBy(@PathVariable("businessId") Long businessId) {
        return businessService.existsBy(businessId);
    }

    @GetMapping("/business/{businessId}")
    public ExternalResBusinessGetByIdDTO getBy(@PathVariable("businessId") Long businessId) {
        return businessService.getBy(businessId);
    }

    @GetMapping("/business")
    public ExternalResBusinessGetByUserIdDTO getByUserId(@RequestParam Long userId) {
        return businessService.getByUserId(userId);
    }
}
