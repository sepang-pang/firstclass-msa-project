package com.first_class.msa.delivery.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first_class.msa.delivery.application.dto.ResGlobalDeliveryAgentDTO;
import com.first_class.msa.delivery.application.dto.ResHubTransitInfoGetDTO;
import com.first_class.msa.delivery.domain.model.Delivery;
import com.first_class.msa.delivery.domain.model.HubDeliveryRoute;
import com.first_class.msa.delivery.domain.valueobject.Sequence;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubDeliveryServiceImpl implements HubDeliveryService {
	private final HubService hubService;
	private final AgentService agentService;

	@Override
	@Transactional
	public List<HubDeliveryRoute> CreateHubDeliveryRoute(Long userId, Delivery delivery) {
		ResHubTransitInfoGetDTO resHubTransitInfoGetDTO
			= hubService.getBy(delivery.getDepartureHubId(), delivery.getArrivalHubId());
		ResGlobalDeliveryAgentDTO resGlobalDeliveryAgentDTO
			= agentService.assignGlobalAgent(delivery.getDepartureHubId());
		int sequenceNum = 0;
		List<HubDeliveryRoute> hubDeliveryRouteList = new ArrayList<>();

		for (ResHubTransitInfoGetDTO.HubTransitInfoDTO hubTransitInfo
			: resHubTransitInfoGetDTO.getHubTransitInfoDTOList()){

			Sequence sequence = new Sequence(++sequenceNum);
			HubDeliveryRoute hubDeliveryRoute = HubDeliveryRoute.createHubDeliveryRoute(
				hubTransitInfo.getDepartureHubId(),
				hubTransitInfo.getArrivalHubId(),
				hubTransitInfo.getDistance(),
				hubTransitInfo.getTransitTime(),
				sequence,
				resGlobalDeliveryAgentDTO.getDeliveryAgentId(),
				delivery
			);
			hubDeliveryRoute.setCreateByAndUpdateBy(userId);

			hubDeliveryRouteList.add(hubDeliveryRoute);
		}

		return hubDeliveryRouteList;
	}



}
