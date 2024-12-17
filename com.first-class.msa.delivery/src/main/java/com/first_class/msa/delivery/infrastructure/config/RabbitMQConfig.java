package com.first_class.msa.delivery.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	public static final String DELIVERY_QUEUE = "delivery.queue";
	public static final String EXCHANGE_NAME = "order.exchange";
	public static final String ORDER_CREATED_DELIVERY_KEY = "order.created.delivery";
	public static final String ORDER_FAILED_KEY = "order.failed";

	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	@Bean
	public Queue deliveryQueue() {
		return new Queue(DELIVERY_QUEUE, true);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	@Bean
	public Queue orderFailedQueue() {
		return new Queue(ORDER_FAILED_KEY, true); // durable = true
	}

	@Bean
	public Binding deliveryBinding(Queue deliveryQueue, TopicExchange exchange) {
		return BindingBuilder.bind(deliveryQueue).to(exchange).with(ORDER_CREATED_DELIVERY_KEY);
	}

	@Bean
	public Binding failedBinding(Queue deliveryQueue, TopicExchange exchange) {
		return BindingBuilder.bind(deliveryQueue).to(exchange).with(ORDER_FAILED_KEY);
	}



}
