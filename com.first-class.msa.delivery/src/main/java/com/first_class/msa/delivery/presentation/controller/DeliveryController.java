package com.first_class.msa.delivery.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first_class.msa.delivery.application.service.DeliveryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery")
public class DeliveryController {
	private final DeliveryService deliveryService;
}
