package com.first_class.msa.hub.domain.repository;

import com.first_class.msa.hub.domain.model.HubTransitInfo;

import java.util.List;
import java.util.Optional;

public interface HubTransitInfoRepository {

    double calculateDistanceByCoordinates(double departureLat, double departureLon, double arrivalLat, double arrivalLon);

    List<HubTransitInfo> findAllByDeletedAtIsNull();

    void save(HubTransitInfo transitInfo);

    Optional<HubTransitInfo> findByIdAndDeletedAtIsNull(Long hubTransitInfoId);
}
