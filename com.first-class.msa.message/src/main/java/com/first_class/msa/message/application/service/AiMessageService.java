package com.first_class.msa.message.application.service;

import com.first_class.msa.message.application.dto.ReqGeminiDTO;
import com.first_class.msa.message.application.dto.ResGeminiDTO;
import com.first_class.msa.message.application.dto.ResHubTransitInfoGetDTO;
import com.first_class.msa.message.application.dto.ResOrderGetDTO;
import com.first_class.msa.message.infrastructure.client.GeminiApiClient;
import com.first_class.msa.message.infrastructure.client.HubClient;
import com.first_class.msa.message.infrastructure.client.OrderClient;
import com.first_class.msa.message.libs.exception.ApiException;
import com.first_class.msa.message.libs.message.ErrorMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AiMessageService {

    private final OrderClient orderClient;
    private final HubClient hubClient;
    private final GeminiApiClient geminiApiClient;
    private final SlackIntegrationService slackIntegrationService;

    public String generateAiMessage(Long userId,Long orderId, Long departureHubId, Long arrivalHubId,String slackChannelId) {

        ResOrderGetDTO orderData = orderClient.getOrder(orderId);
        if (orderData == null) throw new ApiException(ErrorMessage.ORDER_NOT_FOUND);

        ResHubTransitInfoGetDTO hubData = hubClient.getBy(departureHubId, arrivalHubId);
        if (hubData == null || hubData.getHubTransitInfoDTOList().isEmpty()) {
            throw new ApiException(ErrorMessage.HUB_TRANSIT_INFO_NOT_FOUND);
        }

        ReqGeminiDTO reqGeminiDTO = createReqGeminiDTO(orderData, hubData);

        ResGeminiDTO resGeminiDTO = geminiApiClient.generateDispatchDeadline(reqGeminiDTO).getBody();
        if (resGeminiDTO == null || resGeminiDTO.getFinalDispatchDeadline() == null) {
            throw new ApiException(ErrorMessage.AI_DATA_GENERATION_FAILED);
        }

        String aiMessage = createAiMessage(orderId, resGeminiDTO, hubData.getHubTransitInfoDTOList());

        slackIntegrationService.sendMessageToChannel(userId, slackChannelId, aiMessage);

        return aiMessage;
    }

    private ReqGeminiDTO createReqGeminiDTO(ResOrderGetDTO orderData, ResHubTransitInfoGetDTO hubData) {

        // 상품 정보 생성
        List<ReqGeminiDTO.ProductInfo> products = orderData.getOrderDTO().getOrderLineDTOList().stream()
                .map(line -> new ReqGeminiDTO.ProductInfo(line.getProductId(), line.getCount()))
                .collect(Collectors.toList());

        // 허브 정보 생성
        List<ReqGeminiDTO.TransitHubInfo> transitHubInfos = hubData.getHubTransitInfoDTOList().stream()
                .map(hubInfo -> new ReqGeminiDTO.TransitHubInfo(
                        hubInfo.getDepartureHubName(),
                        hubInfo.getArrivalHubName(),
                        hubInfo.getTransitTime(),
                        hubInfo.getDistance()))
                .collect(Collectors.toList());

        return new ReqGeminiDTO(
                orderData.getOrderDTO().getReqInfo(),
                products,
                transitHubInfos
        );
    }

    private String createAiMessage(Long orderId, ResGeminiDTO resGeminiDTO, List<ResHubTransitInfoGetDTO.HubTransitInfoDTO> hubTransitInfoList) {
        // 발송지, 도착지, 경유지 정보 조회
        String departureHubName = hubTransitInfoList.get(0).getDepartureHubName();
        String arrivalHubName = hubTransitInfoList.get(hubTransitInfoList.size() - 1).getArrivalHubName();

        // 경유지 정보 생성
        String transitHubs = hubTransitInfoList.stream()
                .map(hub -> hub.getDepartureHubName() + " -> " + hub.getArrivalHubName())
                .collect(Collectors.joining(" -> "));

        return String.format(
                "📦 주문 ID: %d\n🚚 최종 발송 시한: %s\n📍 발송지: %s\n📍 도착지: %s\n📍 경유지: %s",
                orderId,
                resGeminiDTO.getFinalDispatchDeadline(),
                departureHubName,
                arrivalHubName,
                transitHubs
        );
    }
}
