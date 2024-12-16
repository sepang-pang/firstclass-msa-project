package com.first_class.msa.product.infrastructure.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	public static final String PRODUCT_QUEUE = "product.queue";
	public static final String EXCHANGE_NAME = "order.exchange";
	public static final String ORDER_CREATED_PRODUCT_KEY = "order.created.product";
	public static final String ORDER_FAILED_KEY = "order.failed";

	@Bean
	public Queue productQueue() {
		return new Queue(PRODUCT_QUEUE, true); // durable = true
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	@Bean
	public Binding productBinding(Queue productQueue, TopicExchange exchange) {
		return BindingBuilder.bind(productQueue).to(exchange).with(ORDER_CREATED_PRODUCT_KEY);
	}

	@Bean
	public Binding failedBinding(Queue productQueue, TopicExchange exchange) {
		return BindingBuilder.bind(productQueue).to(exchange).with(ORDER_FAILED_KEY);
	}

}
