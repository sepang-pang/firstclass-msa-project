package com.first_class.msa.hub.application.service;


import com.first_class.msa.hub.application.dto.transit.HubNode;
import com.first_class.msa.hub.application.dto.transit.ResHubTransitInfoGetDTO;
import com.first_class.msa.hub.application.dto.transit.ResHubTransitInfoPostDTO;
import com.first_class.msa.hub.domain.model.Hub;
import com.first_class.msa.hub.domain.model.HubTransitInfo;
import com.first_class.msa.hub.domain.repository.HubRepository;
import com.first_class.msa.hub.domain.repository.HubTransitInfoRepository;
import com.first_class.msa.hub.presentation.request.transit.ReqHubTransitInfoPostDTO;
import com.first_class.msa.hub.presentation.request.transit.ReqHubTransitInfoPutByIdDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Transactional(readOnly = true)
    @Cacheable(value = "hubTransitCache", key = "#departureHubId + '-' + #arrivalHubId")
    public ResHubTransitInfoGetDTO getBy(Long departureHubId, Long arrivalHubId) {
        // NOTE : 전체 조회
        List<HubTransitInfo> hubTransitInfos = hubTransitInfoRepository.findAllByDeletedAtIsNull();

        // NOTE : 그래프 구성
        Map<Long, List<HubTransitInfo>> graph = buildGraph(hubTransitInfos);

        // NOTE : 다익스트라 알고리즘 수행
        List<HubTransitInfo> shortestPath = dijkstra(departureHubId, arrivalHubId, graph);

        // NOTE : DTO 변환 및 반환
        return ResHubTransitInfoGetDTO.of(shortestPath);
    }

    @Transactional
    public void putBy(Long userId, String account, Long hubTransitInfoId, ReqHubTransitInfoPutByIdDTO dto) {
        // NOTE : 권한 검증
        validateUserRole(userId);

        // NOTE : 수정할 허브 간 이동 정보 조회
        HubTransitInfo hubTransitInfoForModification = getHubTransitInfoBy(hubTransitInfoId);

        Long departureHubId = dto.getHubTransitInfoDTO().getDepartureHubId();
        Long arrivalHubId = dto.getHubTransitInfoDTO().getArrivalHubId();

        // NOTE : 출발/도착 허브가 변경된 경우에만 거리 계산 및 소요 시간 계산
        if (isHubInfoChanged(hubTransitInfoForModification, departureHubId, arrivalHubId)) {

            // NOTE : 출발 허브 및 도착 허브 정보 가져오기
            Hub departureHub = getHubBy(departureHubId);
            Hub arrivalHub = getHubBy(arrivalHubId);

            // NOTE : 거리 계산
            double calculateDistance = getDistanceBy(
                    departureHub.getLatitude(),
                    departureHub.getLongitude(),
                    arrivalHub.getLatitude(),
                    arrivalHub.getLongitude()
            );

            // NOTE : 소요 시간 계산
            Long transitTime = calculateTransitTime(calculateDistance);

            // NOTE : 변경된 값으로 허브 간 이동 정보 수정
            hubTransitInfoForModification.updateHubTransitInfo(
                    departureHub,
                    arrivalHub,
                    transitTime,
                    calculateDistance,
                    account
            );
        }
    }

    private void validateUserRole(Long userId) {
        if (!Objects.equals("MASTER", authService.getRoleBy(userId).getRole())) {
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

    private static Map<Long, List<HubTransitInfo>> buildGraph(List<HubTransitInfo> hubTransitInfos) {
        Map<Long, List<HubTransitInfo>> graph = new HashMap<>();
        for (HubTransitInfo info : hubTransitInfos) {
            graph.computeIfAbsent(info.getDepartureHub().getId(), k -> new ArrayList<>()).add(info);
        }
        return graph;
    }

    private static List<HubTransitInfo> dijkstra(Long departureHubId, Long arrivalHubId, Map<Long, List<HubTransitInfo>> graph) {
        Map<Long, Double> distances = new HashMap<>();
        Map<Long, HubTransitInfo> previous = new HashMap<>();
        PriorityQueue<HubNode> pq = new PriorityQueue<>(Comparator.comparingDouble(HubNode::getDistance));

        for (Long hubId : graph.keySet()) {
            distances.put(hubId, Double.MAX_VALUE);
        }
        distances.put(departureHubId, 0.0);
        pq.add(
                HubNode.builder()
                        .hubId(departureHubId)
                        .distance(0.0)
                        .build()
        );

        // 다익스트라 알고리즘 실행
        while (!pq.isEmpty()) {
            HubNode currentNode = pq.poll();
            Long currentHubId = currentNode.getHubId();

            if (currentHubId.equals(arrivalHubId)) {
                break;
            }

            if (graph.containsKey(currentHubId)) {
                for (HubTransitInfo neighbor : graph.get(currentHubId)) {
                    Long neighborId = neighbor.getArrivalHub().getId();
                    double newDist = distances.get(currentHubId) + neighbor.getDistance();

                    if (newDist < distances.get(neighborId)) {
                        distances.put(neighborId, newDist);
                        previous.put(neighborId, neighbor);
                        pq.add(
                                HubNode.builder()
                                        .hubId(neighborId)
                                        .distance(newDist)
                                        .build()
                        );
                    }
                }
            }
        }

        // 경로 추적
        List<HubTransitInfo> shortestPath = new ArrayList<>();
        Long currentHubId = arrivalHubId;
        while (previous.containsKey(currentHubId)) {
            HubTransitInfo info = previous.get(currentHubId);
            shortestPath.add(info);
            currentHubId = info.getDepartureHub().getId();
        }

        // 경로가 존재하지 않을 경우
        if (shortestPath.isEmpty()) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }

        // 경로 순서를 뒤집음 (출발 -> 도착 순으로)
        Collections.reverse(shortestPath);
        return shortestPath;
    }

    private HubTransitInfo getHubTransitInfoBy(Long hubTransitInfoId) {
        return hubTransitInfoRepository.findByIdAndDeletedAtIsNull(hubTransitInfoId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 허브 간 이동 정보입니다."));
    }

    private boolean isHubInfoChanged(HubTransitInfo existingInfo, Long departureHubId, Long arrivalHubId) {
        boolean isDepartureHubChanged = !Objects.equals(existingInfo.getDepartureHub().getId(), departureHubId);
        boolean isArrivalHubChanged = !Objects.equals(existingInfo.getArrivalHub().getId(),arrivalHubId);
        return isDepartureHubChanged || isArrivalHubChanged;
    }
}
