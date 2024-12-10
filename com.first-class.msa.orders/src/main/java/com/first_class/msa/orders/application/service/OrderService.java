package com.first_class.msa.orders.application.service;

import org.springframework.stereotype.Service;

import com.first_class.msa.orders.domain.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
}
