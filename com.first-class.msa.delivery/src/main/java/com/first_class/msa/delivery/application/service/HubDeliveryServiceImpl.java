package com.first_class.msa.delivery.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.delivery.application.dto.ResGlobalDeliveryAgentDTO;
import com.first_class.msa.delivery.application.dto.ResHubTransitInfoDTO;
import com.first_class.msa.delivery.domain.model.Delivery;
import com.first_class.msa.delivery.domain.model.HubDeliveryRoute;
import com.first_class.msa.delivery.domain.valueobject.Sequence;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubDeliveryServiceImpl implements HubDeliveryService {
	private final HubService hubService;
	private final DeliveryAgentService deliveryAgentService;

	@Override
	@Transactional
	public List<HubDeliveryRoute> CreateHubDeliveryRoute(Delivery delivery) {
		// TODO: 2024-12-15 시작 허브 ID 최종 도착 허브 ID 요청 -> 경로 허브 ID 반환하는 feign client 필요
		ResHubTransitInfoDTO resHubTransitInfoDTO
			= hubService.postHubTransitInfo(delivery.getDepartureHubId(), delivery.getArrivalHubId());
		ResGlobalDeliveryAgentDTO resGlobalDeliveryAgentDTO = deliveryAgentService.assignGlobalAgent();
		int sequenceNum = 0;
		List<HubDeliveryRoute> hubDeliveryRouteList = new ArrayList<>();

		for (ResHubTransitInfoDTO.HubTransitInfoList.HubTransitInfo hubTransitInfo
			: resHubTransitInfoDTO.getHubTransitInfo().getHubTransitInfoLIst()){

			Sequence sequence = new Sequence(++sequenceNum);
			hubDeliveryRouteList.add(
				HubDeliveryRoute.createHubDeliveryRoute(
				hubTransitInfo.getDepartureHubId(),
				hubTransitInfo.getArrivalHubId(),
				hubTransitInfo.getDistance(),
				hubTransitInfo.getTransitTime(),
				sequence,
				resGlobalDeliveryAgentDTO.getDeliveryAgent(),
				delivery
				)
			);
		}

		return hubDeliveryRouteList;
	}

}
