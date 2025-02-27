package com.first_class.msa.delivery.application.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.delivery.application.dto.ResDeliveryOrderSearchDTO;
import com.first_class.msa.delivery.application.dto.ResDeliverySearchDTO;
import com.first_class.msa.delivery.application.dto.ResRoleGetByIdDTO;
import com.first_class.msa.delivery.domain.common.BusinessDeliveryStatus;
import com.first_class.msa.delivery.domain.common.DeliveryStatus;
import com.first_class.msa.delivery.domain.common.HubDeliveryStatus;
import com.first_class.msa.delivery.domain.common.UserRole;
import com.first_class.msa.delivery.domain.model.BusinessDeliveryRoute;
import com.first_class.msa.delivery.domain.model.Delivery;
import com.first_class.msa.delivery.domain.model.HubDeliveryRoute;
import com.first_class.msa.delivery.domain.repository.DeliveryRepository;
import com.first_class.msa.delivery.infrastructure.config.RabbitMQConfig;
import com.first_class.msa.delivery.infrastructure.event.OrderCancelDeliveryEvent;
import com.first_class.msa.delivery.infrastructure.event.OrderCreateDeliveryEvent;
import com.first_class.msa.delivery.libs.exception.ApiException;
import com.first_class.msa.delivery.libs.message.ErrorMessage;
import com.first_class.msa.delivery.presentation.dto.ReqBusinessDeliveryPutDTO;
import com.first_class.msa.delivery.presentation.dto.ReqHubDeliveryPutDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

	private final DeliveryRepository deliveryRepository;
	private final HubDeliveryService hubDeliveryService;
	private final BusinessDeliveryService businessDeliveryService;
	private final AuthConditionService authConditionService;
	private final AuthService authService;
	private final AgentService agentService;

	@Override
	@RabbitListener(queues = RabbitMQConfig.DELIVERY_QUEUE)
	@Transactional
	public void handleOrderCreateDeliveryEvent(OrderCreateDeliveryEvent event) {
		log.info("메세지 수신" + event);
		Delivery delivery = Delivery.createDelivery(
			event.getOrderId(),
			event.getDepartureHubId(),
			event.getArrivalHubId()
		);
		List<HubDeliveryRoute> hubDeliveryRouteList
			= hubDeliveryService.CreateHubDeliveryRoute(event.getUserId(), delivery);

		BusinessDeliveryRoute businessDeliveryRoute
			= businessDeliveryService.createBusinessDeliveryRoute(
			event.getUserId(),
			event.getArrivalHubId(),
			event.getDeliveryBusinessId(),
			event.getAddress()
		);

		delivery.addHubDeliveryRouteList(hubDeliveryRouteList);
		delivery.addBusinessDeliveryRoute(businessDeliveryRoute);
		delivery.addCreatedBy(event.getUserId());
		delivery.addUpdatedBy(event.getUserId());

		deliveryRepository.save(delivery);

	}

	@Override
	@Transactional
	public void hubRouteStatusPutBy(
		Long userId, Long deliveryId,
		Long hubDeliveryRouteId,
		ReqHubDeliveryPutDTO reqHubDeliveryPutDTO
	) {
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);
		Delivery delivery = findByIdANDIsNotNull(deliveryId);
		authConditionService.validateHubStatusPutByAuthCondition(
			UserRole.valueOf(resRoleGetByIdDTO.getRole()),
			userId,
			delivery);
		if (delivery.updateHubDeliveryRouteState(
			userId,
			hubDeliveryRouteId,
			reqHubDeliveryPutDTO.getHubDeliveryStatus()
		)
		) {
			businessDeliveryService.assignAgentToBusinessDeliveryRoute(delivery.getBusinessDeliveryRoute());
			delivery.getBusinessDeliveryRoute().updateBusinessDeliveryStatus(BusinessDeliveryStatus.READY);
			// AI slack 완성되면 message 작성
		}
	}

	@Override
	@Transactional
	public void businessDeliveryStatusPutBy(
		Long userId,
		Long deliveryId,
		Long businessDeliveryRouteId,
		ReqBusinessDeliveryPutDTO reqBusinessDeliveryPutDTO
	) {
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);
		Delivery delivery = findByIdANDIsNotNull(deliveryId);
		authConditionService.validateBusinessStatusPutByAuthCondition(
			UserRole.valueOf(resRoleGetByIdDTO.getRole()),
			userId,
			delivery
		);
		delivery.updateBusinessDeliveryRouteStatus(
			userId,
			businessDeliveryRouteId,
			reqBusinessDeliveryPutDTO.getBusinessDeliveryStatus()
		);
	}

	@Override
	@Transactional(readOnly = true)
	public ResDeliverySearchDTO getSearchById(Long userId, Long deliveryId) {
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);
		Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
			() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOF_FOUND_DELIVERY))
		);
		authConditionService.validateSearchByAuthCondition(UserRole.valueOf(resRoleGetByIdDTO.getRole()), userId,
			delivery);
		return ResDeliverySearchDTO.from(delivery);
	}

	private Delivery findByIdANDIsNotNull(Long deliveryId) {
		return deliveryRepository.findByIdAndIsNULL(deliveryId).orElseThrow(
			() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOF_FOUND_DELIVERY)));
	}

	@Override
	@Transactional
	public void deleteDeliveryById(Long userId, Long deliveryId) {
		ResRoleGetByIdDTO resRoleGetByIdDTO = authService.getRoleBy(userId);
		Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
			() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOF_FOUND_DELIVERY))
		);
		if (!delivery.getDeliveryStatus().equals(DeliveryStatus.READY)
			&&
			!delivery.
				getHubDeliveryRouteList()
				.get(0)
				.getHubDeliveryStatus()
				.equals(HubDeliveryStatus.WAITING_FOR_TRANSIT)
		) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.NOF_FOUND_DELIVERY));
		}
		authConditionService.validateDeleteByAuthCondition(
			UserRole.valueOf(resRoleGetByIdDTO.getRole()),
			userId,
			delivery
		);
		delivery.updateDeliveryStatus(DeliveryStatus.CANCELLED);
		delivery.setDeleteAllHubDeliveryRoute(userId);
		delivery.getBusinessDeliveryRoute().setBusinessDeliveryRoute(userId);
		delivery.setDeleteDelivery(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public ResDeliveryOrderSearchDTO getAllDeliveryBy(Long userId) {
		Long deliveryAgentId = agentService.getDeliveryAgentByUserId(userId).getDeliveryAgentId();
		List<Long> deliveryList = deliveryRepository.findOrderIdsByAgentId(deliveryAgentId);

		return ResDeliveryOrderSearchDTO.from(deliveryList);

	}

	@Override
	public boolean existDeliveryBy(Long orderId, Long userId) {
		Long deliveryId = agentService.getDeliveryAgentByUserId(userId).getDeliveryAgentId();
		return deliveryRepository.existsByIdAndOrderId(deliveryId, orderId);
	}

	@Transactional
	@RabbitListener(queues = RabbitMQConfig.ORDER_FAILED_KEY)
	public void cancelOrder(OrderCancelDeliveryEvent event) {
		Delivery delivery = deliveryRepository.findByOrderId(event.getOrderId()).orElseThrow(
			() -> new IllegalArgumentException(new ApiException(ErrorMessage.NOF_FOUND_DELIVERY))
		);
		delivery.setDeleteAllHubDeliveryRoute(event.getUserId());
		delivery.getBusinessDeliveryRoute().setBusinessDeliveryRoute(event.getUserId());
		delivery.setDeleteDelivery(event.getUserId());

	}

}
