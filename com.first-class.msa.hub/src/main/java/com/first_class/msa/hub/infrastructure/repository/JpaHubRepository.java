package com.first_class.msa.hub.infrastructure.repository;

import com.first_class.msa.hub.domain.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaHubRepository extends JpaRepository<Hub, Long>, HubQueryBinder {

    boolean existsByLatitudeAndLongitudeAndDeletedAtIsNull(double latitude, double longitude);

    Optional<Hub> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByIdAndDeletedAtIsNull(Long hubId);

    @Query("select h.id from Hub h where h.managerId = :userId and h.deletedAt is null")
    Long findIdByManagerIdAndDeletedAtIsNull(@Param("userId") Long userId);

}
