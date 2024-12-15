package com.first_class.msa.delivery.application.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.delivery.application.dto.ResRoleGetByIdDTO;
import com.first_class.msa.delivery.domain.common.HubStatus;
import com.first_class.msa.delivery.domain.common.UserRole;
import com.first_class.msa.delivery.domain.model.BusinessDeliveryRoute;
import com.first_class.msa.delivery.domain.model.Delivery;
import com.first_class.msa.delivery.domain.model.HubDeliveryRoute;
import com.first_class.msa.delivery.domain.repository.DeliveryRepository;
import com.first_class.msa.delivery.infrastructure.config.RabbitMQConfig;
import com.first_class.msa.delivery.infrastructure.event.OrderCreateDeliveryEvent;
import com.first_class.msa.delivery.libs.exception.ApiException;
import com.first_class.msa.delivery.libs.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

	private final DeliveryRepository deliveryRepository;
	private final HubDeliveryService hubDeliveryService;
	private final BusinessDeliveryService businessDeliveryService;
	private final AuthConditionService authConditionService;
	private final AuthService authService;

	@Override
	@RabbitListener(queues = RabbitMQConfig.DELIVERY_QUEUE)
	@Transactional
	public void handleOrderCreateDeliveryEvent(OrderCreateDeliveryEvent event) {

		Delivery delivery = Delivery.createDelivery(
			event.getOrderId(),
			event.getDepartureHubId(),
			event.getArrivalHubId()
		);
		List<HubDeliveryRoute> hubDeliveryRouteList = hubDeliveryService.CreateHubDeliveryRoute(delivery);
		BusinessDeliveryRoute businessDeliveryRoute
			= businessDeliveryService.createBusinessDeliveryRoute(
				event.getArrivalHubId(),
				event.getDeliveryBusinessId(),
				event.getAddress()
		);
		delivery.addHubDeliveryRouteList(hubDeliveryRouteList);
		delivery.addBusinessDeliveryRoute(businessDeliveryRoute);
		deliveryRepository.save(delivery);

	}


	@Override
	@Transactional
	public void HubStatusRoutePutBy(Long userId, Long deliveryId, Long hubDeliveryRouteId, HubStatus hubStatus) {
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);
		Delivery delivery = findById(deliveryId);
		authConditionService.HubStatusPutByAuthCondition(
			UserRole.valueOf(resRoleGetByIdDTO.getRole()),
			userId,
			delivery);
		if (delivery.updateHubDeliveryRouteState(hubDeliveryRouteId, hubStatus)) {
			businessDeliveryService.assignAgentToBusinessDeliveryRoute(delivery.getBusinessDeliveryRoute());
			// AI slack 완성되면 message 작성
		}
	}

	private Delivery findById(Long deliveryId) {
		return deliveryRepository.findById(deliveryId).orElseThrow(
			() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOF_FOUND_DELIVERY)));
	}
}
