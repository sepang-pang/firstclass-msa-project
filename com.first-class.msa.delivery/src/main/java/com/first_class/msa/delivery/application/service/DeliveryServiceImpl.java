package com.first_class.msa.delivery.application.service;

import org.springframework.stereotype.Service;

import com.first_class.msa.delivery.domain.repository.DeliveryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService{

	private final DeliveryRepository deliveryRepository;
}
