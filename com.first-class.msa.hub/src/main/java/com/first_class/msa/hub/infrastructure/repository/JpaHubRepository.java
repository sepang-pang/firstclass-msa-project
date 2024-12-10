package com.first_class.msa.hub.infrastructure.repository;

import com.first_class.msa.hub.domain.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaHubRepository extends JpaRepository<Hub, Long> {

    boolean existsByLatitudeAndLongitudeAndDeletedAtIsNull(double latitude, double longitude);

    Optional<Hub> findByIdAndDeletedAtIsNull(Long id);
}
