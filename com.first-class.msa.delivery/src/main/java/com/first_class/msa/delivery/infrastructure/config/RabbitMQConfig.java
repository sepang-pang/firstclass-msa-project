package com.first_class.msa.delivery.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	public static final String DELIVERY_QUEUE = "delivery.queue";
	public static final String EXCHANGE_NAME = "order.exchange";
	public static final String ORDER_CREATED_DELIVERY_KEY = "order.created.delivery";

	@Bean
	public Queue deliveryQueue() {
		return new Queue(DELIVERY_QUEUE, true);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	@Bean
	public Binding deliveryBinding(Queue deliveryQueue, TopicExchange exchange) {
		return BindingBuilder.bind(deliveryQueue).to(exchange).with(ORDER_CREATED_DELIVERY_KEY);
	}
}
