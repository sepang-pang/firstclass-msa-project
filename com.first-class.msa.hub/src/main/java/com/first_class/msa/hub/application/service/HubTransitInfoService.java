package com.first_class.msa.hub.application.service;


import com.first_class.msa.hub.application.dto.transit.ResHubTransitInfoPostDTO;
import com.first_class.msa.hub.domain.model.Hub;
import com.first_class.msa.hub.domain.model.HubTransitInfo;
import com.first_class.msa.hub.domain.repository.HubRepository;
import com.first_class.msa.hub.domain.repository.HubTransitInfoRepository;
import com.first_class.msa.hub.presentation.request.transit.ReqHubTransitInfoPostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HubTransitInfoService {

    private final HubRepository hubRepository;
    private final HubTransitInfoRepository hubTransitInfoRepository;
    private final AuthService authService;


    @Transactional
    public ResHubTransitInfoPostDTO postBy(Long userId, String account, ReqHubTransitInfoPostDTO dto) {

        validateUserRole(userId);

        Hub departureHub = getHubBy(dto.getReqHubTransitInfoDTO().getDepartureHubId());
        Hub arrivalHub = getHubBy(dto.getReqHubTransitInfoDTO().getArrivalHubId());

        double departureHubLatitude = departureHub.getLatitude();
        double departureHubLongitude = departureHub.getLongitude();

        double arrivalHubLatitude = arrivalHub.getLatitude();
        double arrivalHubLongitude = arrivalHub.getLongitude();

        // NOTE : 거리 계산
        double calculateDistance = getDistanceBy(departureHubLatitude, departureHubLongitude, arrivalHubLatitude, arrivalHubLongitude);

        // NOTE : 소요 시간 계산
        Long transitTime = calculateTransitTime(calculateDistance);

        HubTransitInfo hubTransitInfoForSaving = HubTransitInfo.createHubTransitInfo(departureHub, arrivalHub, transitTime, calculateDistance, account);

        hubTransitInfoRepository.save(hubTransitInfoForSaving);

        return ResHubTransitInfoPostDTO.of(hubTransitInfoForSaving);
    }

    private void validateUserRole(Long userId) {
        if (Objects.equals("MASTER", authService.getRoleBy(userId).getRole())) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
    }

    private Hub getHubBy(Long hubId) {
        return hubRepository.findByIdAndDeletedAtIsNull(hubId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 허브입니다."));
    }

    private double getDistanceBy(double departureHubLatitude, double departureHubLongitude, double arrivalHubLatitude, double arrivalHubLongitude) {
        return hubTransitInfoRepository.calculateDistanceByCoordinates(departureHubLatitude, departureHubLongitude, arrivalHubLatitude, arrivalHubLongitude) / 1000;
    }

    private Long calculateTransitTime(double distance) {
        double timeInHours = distance / 60;
        return (long) (timeInHours * 3600);
    }


}
