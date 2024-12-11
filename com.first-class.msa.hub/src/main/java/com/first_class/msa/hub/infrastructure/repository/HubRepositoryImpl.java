package com.first_class.msa.hub.infrastructure.repository;

import com.first_class.msa.hub.domain.model.Hub;
import com.first_class.msa.hub.domain.repository.HubRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final JpaHubRepository jpaHubRepository;

    @Override
    public boolean existsByLatitudeAndLongitudeAndDeletedIsNull(double latitude, double longitude) {
        return jpaHubRepository.existsByLatitudeAndLongitudeAndDeletedAtIsNull(latitude, longitude);
    }

    @Override
    public Optional<Hub> findByIdAndDeletedAtIsNull(Long id) {
        return jpaHubRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Page<Hub> findAll(Predicate predicate, Pageable pageable) {
        return jpaHubRepository.findAll(predicate, pageable);
    }

    @Override
    public boolean existsByIdAndDeletedAtIsNull(Long hubId) {
        return jpaHubRepository.existsByIdAndDeletedAtIsNull(hubId);
    }

    @Override
    public Long findByManagerIdAndDeletedAtIsNull(Long userId) {
        return jpaHubRepository.findByManagerIdAndDeletedAtIsNull(userId);
    }

    @Override
    public Hub save(Hub hub) {
        return jpaHubRepository.save(hub);
    }
}
