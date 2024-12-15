package com.first_class.msa.hub.infrastructure.repository.HubTransitInfo;

import com.first_class.msa.hub.domain.model.HubTransitInfo;
import com.first_class.msa.hub.domain.repository.HubTransitInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubTransitInfoRepositoryImpl implements HubTransitInfoRepository {

    private final JpaHubTransitInfoRepository jpaHubTransitInfoRepository;


    @Override
    public double calculateDistanceByCoordinates(double departureLat, double departureLon, double arrivalLat, double arrivalLon) {
        return jpaHubTransitInfoRepository.calculateDistanceByCoordinates(departureLat, departureLon, arrivalLat, arrivalLon);
    }

    @Override
    public void save(HubTransitInfo hubTransitInfo) {
        jpaHubTransitInfoRepository.save(hubTransitInfo);
    }

}
