package com.first_class.msa.orders.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	public static final String EXCHANGE_NAME = "order.exchange";


	// 라우팅 키
	public static final String ORDER_CREATED_PRODUCT_KEY = "order.created.product";

	public static final String ORDER_DELETED_PRODUCT_KEY = "order.deleted.product";

	public static final String ORDER_CREATED_DELIVERY_KEY = "order.created.delivery";

	public static final String ORDER_DELETED_DELIVERY_KEY = "order.deleted.delivery";

	public static final String ORDER_FAILED_KEY = "order.failed";

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}


	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
	@Bean
	public Queue failedQueue() {
		return new Queue(ORDER_FAILED_KEY, true); // durable = true
	}

	@Bean
	public Binding failedBinding(Queue failedQueue, TopicExchange exchange) {
		return BindingBuilder.bind(failedQueue).to(exchange).with(ORDER_FAILED_KEY);
	}


}
