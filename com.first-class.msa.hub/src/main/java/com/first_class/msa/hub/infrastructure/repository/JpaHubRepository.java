package com.first_class.msa.hub.infrastructure.repository;

import com.first_class.msa.hub.domain.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaHubRepository extends JpaRepository<Hub, Long> {

    boolean existsByLatitudeAndLongitudeAndDeletedAtIsNull(double latitude, double longitude);
}
