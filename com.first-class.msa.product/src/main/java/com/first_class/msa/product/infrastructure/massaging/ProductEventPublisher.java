package com.first_class.msa.product.infrastructure.massaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.first_class.msa.product.infrastructure.configuration.RabbitMQConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {
	private final RabbitTemplate rabbitTemplate;

	public void publishFailedOrder(Long orderId) {
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ORDER_FAILED_KEY, orderId);
		log.info("주문 생성 실패 메세지");
	}
}
