package com.first_class.msa.hub.domain.repository;

import com.first_class.msa.hub.domain.model.Hub;

import java.util.Optional;

public interface HubRepository {

    boolean existsByLatitudeAndLongitudeAndDeletedIsNull(double latitude, double longitude);

    Optional<Hub> findByIdAndDeletedAtIsNull(Long id);

    Hub save(Hub hub);
}
