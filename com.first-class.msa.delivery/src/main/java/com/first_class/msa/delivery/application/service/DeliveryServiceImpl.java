package com.first_class.msa.delivery.application.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.delivery.domain.model.Delivery;
import com.first_class.msa.delivery.domain.model.HubDeliveryRoute;
import com.first_class.msa.delivery.domain.repository.DeliveryRepository;
import com.first_class.msa.delivery.domain.valueobject.Address;
import com.first_class.msa.delivery.infrastructure.config.RabbitMQConfig;
import com.first_class.msa.delivery.infrastructure.event.OrderCreateDeliveryEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService{

	private final DeliveryRepository deliveryRepository;
	private final HubDeliveryService hubDeliveryService;

	@Override
	@RabbitListener(queues = RabbitMQConfig.DELIVERY_QUEUE)
	@Transactional
	public void handleOrderCreateDeliveryEvent(OrderCreateDeliveryEvent event){

		Delivery delivery = Delivery.createDelivery(
			event.getOrderId(),
			event.getDepartureHubId(),
			event.getArrivalHubId(),
			event.getDeliveryBusinessId(),
			new Address(event.getAddress())
		);
		List<HubDeliveryRoute> hubDeliveryRouteList = hubDeliveryService.CreateHubDeliveryRoute(delivery);
		delivery.updateHubDeliveryRouteList(hubDeliveryRouteList);

		deliveryRepository.save(delivery);

	}
}
