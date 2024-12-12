package com.first_class.msa.agent.application.service;

import org.springframework.stereotype.Service;

import com.first_class.msa.agent.domain.repository.DeliveryAgentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryAgentServiceImpl implements DeliveryAgentService {

	private final DeliveryAgentRepository deliveryAgentRepository;

}
