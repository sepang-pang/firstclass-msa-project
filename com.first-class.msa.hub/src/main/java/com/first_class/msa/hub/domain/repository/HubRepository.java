package com.first_class.msa.hub.domain.repository;

import com.first_class.msa.hub.domain.model.Hub;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface HubRepository {

    boolean existsByLatitudeAndLongitudeAndDeletedIsNull(double latitude, double longitude);

    Optional<Hub> findByIdAndDeletedAtIsNull(Long id);

    Long findByManagerIdAndDeletedAtIsNull(Long userId);

    Hub save(Hub hub);

    Page<Hub> findAll(Predicate predicate, Pageable pageable);

    boolean existsByIdAndDeletedAtIsNull(Long hubId);
}
