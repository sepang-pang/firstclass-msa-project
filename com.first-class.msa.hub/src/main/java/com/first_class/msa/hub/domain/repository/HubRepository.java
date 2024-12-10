package com.first_class.msa.hub.domain.repository;

import com.first_class.msa.hub.domain.model.Hub;

public interface HubRepository {

    boolean existsByLatitudeAndLongitudeAndDeletedIsNull(double latitude, double longitude);

    void save(Hub hub);
}
