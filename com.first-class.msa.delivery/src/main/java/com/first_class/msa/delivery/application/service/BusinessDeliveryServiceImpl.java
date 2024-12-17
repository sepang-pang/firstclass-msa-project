package com.first_class.msa.delivery.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.delivery.application.dto.ResNaverRouteInfoDTO;
import com.first_class.msa.delivery.application.dto.ResHubDeliveryAgentDTO;
import com.first_class.msa.delivery.application.dto.ResHubInfoGetDTO;
import com.first_class.msa.delivery.domain.model.BusinessDeliveryRoute;
import com.first_class.msa.delivery.domain.valueobject.Address;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessDeliveryServiceImpl implements BusinessDeliveryService {
	private final AgentService agentService;
	private final NaverDirectionsService naverDirectionsService;
	private final HubService hubService;

	@Override
	@Transactional
	public BusinessDeliveryRoute createBusinessDeliveryRoute(
		Long userId,
		Long arrivalHubId,
		Long businessId,
		String address
	) {
		Address arivalAddress = new Address(address);
		BusinessDeliveryRoute businessDeliveryRoute =
			BusinessDeliveryRoute.createBusinessDeliveryRoute(arrivalHubId, businessId, arivalAddress);
		businessDeliveryRoute.setCreateByAndUpdateBy(userId);

		return BusinessDeliveryRoute.createBusinessDeliveryRoute(arrivalHubId, businessId, arivalAddress);
	}

	@Override
	@Transactional
	public void assignAgentToBusinessDeliveryRoute(BusinessDeliveryRoute businessDeliveryRoute) {
		// 네이버 API 를통해 주소 반환
		// 허브 위도 경도 요청 임시로 작성
		ResHubInfoGetDTO resHubInfoGetDTO = hubService.getHubInfoBy(businessDeliveryRoute.getDepartureHubId());
		log.info("Latitude : " + resHubInfoGetDTO.getLatitude());
		log.info("Longitude : " + resHubInfoGetDTO.getLongitude());
		log.info("address : " + businessDeliveryRoute.getAddress().getValue());
		ResNaverRouteInfoDTO resNaverRouteInfoDTO
			= naverDirectionsService.getRouteInfo(
			resHubInfoGetDTO.getLatitude(),
			resHubInfoGetDTO.getLongitude(),
			businessDeliveryRoute.getAddress().getValue()
		);

		ResHubDeliveryAgentDTO resHubDeliveryAgentDTO = agentService.assignHubAgent(
			businessDeliveryRoute.getDepartureHubId());

		businessDeliveryRoute.assignDeliveryAgentId(resHubDeliveryAgentDTO.getDeliveryAgentId());

		businessDeliveryRoute.updateExpectedTimeAndDistance(
			resNaverRouteInfoDTO.getDuration(),
			resNaverRouteInfoDTO.getDistance()
		);

	}
}
