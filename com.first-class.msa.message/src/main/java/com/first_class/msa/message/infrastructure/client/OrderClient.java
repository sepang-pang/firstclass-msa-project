package com.first_class.msa.message.infrastructure.client;

import com.first_class.msa.message.application.dto.ResOrderGetDTO;
import com.first_class.msa.message.application.service.OrderService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderClient extends OrderService {

    @GetMapping("/external/order/{orderId}")
    ResOrderGetDTO getOrder(@PathVariable("orderId") Long orderId);

}
