package com.first_class.msa.hub.domain.repository;

import com.first_class.msa.hub.domain.model.HubTransitInfo;

public interface HubTransitInfoRepository {

    double calculateDistanceByCoordinates(double departureLat, double departureLon, double arrivalLat, double arrivalLon);

    void save(HubTransitInfo transitInfo);
}
