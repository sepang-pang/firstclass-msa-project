package com.first_class.msa.message.infrastructure.client;

import com.first_class.msa.message.application.dto.ReqGeminiDTO;
import com.first_class.msa.message.application.dto.ResGeminiDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gemini-api", url = "http://gemini-api.example.com")
public interface GeminiApiClient {

    @PostMapping("/generate-dispatch-deadline")
    ResponseEntity<ResGeminiDTO> generateDispatchDeadline(@RequestBody ReqGeminiDTO reqGeminiDTO);
}
