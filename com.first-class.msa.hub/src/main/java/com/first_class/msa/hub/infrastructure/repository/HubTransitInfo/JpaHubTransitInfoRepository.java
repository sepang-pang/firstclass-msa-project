package com.first_class.msa.hub.infrastructure.repository.HubTransitInfo;

import com.first_class.msa.hub.domain.model.HubTransitInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaHubTransitInfoRepository extends JpaRepository<HubTransitInfo, Long> {

    @Query(value = "select ST_DistanceSphere(" +
            "ST_MakePoint(:departureLongitude, :departureLatitude), " +
            "ST_MakePoint(:arrivalLongitude, :arrivalLatitude))", nativeQuery = true)
    double calculateDistanceByCoordinates(@Param("departureLatitude") double departureLatitude,
                                          @Param("departureLongitude") double departureLongitude,
                                          @Param("arrivalLatitude") double arrivalLatitude,
                                          @Param("arrivalLongitude") double arrivalLongitude);


    List<HubTransitInfo> findAllByDeletedAtIsNull();

    Optional<HubTransitInfo> findByIdAndDeletedAtIsNull(Long id);
}
