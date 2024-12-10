package com.first_class.msa.hub.domain.repository;

import com.first_class.msa.hub.domain.model.Hub;

public interface HubRepository {

    boolean existsByLatitudeAndLongitudeAndDeletedIsNull(double latitude, double longitude);

    Hub save(Hub hub);
}
