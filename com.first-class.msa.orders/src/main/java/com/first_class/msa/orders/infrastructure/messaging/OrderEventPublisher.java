package com.first_class.msa.orders.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.first_class.msa.orders.infrastructure.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {
	private final RabbitTemplate rabbitTemplate;

	public void publishEvent(String routingKey, Object message) {
		rabbitTemplate.convertAndSend(
			RabbitMQConfig.EXCHANGE_NAME,
			routingKey,
			message
		);
		log.info("이벤트 메세지 발생 key {}, message {}", routingKey, message);
	}
}
