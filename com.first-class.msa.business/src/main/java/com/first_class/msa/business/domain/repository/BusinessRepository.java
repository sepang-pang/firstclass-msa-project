package com.first_class.msa.business.domain.repository;

import com.first_class.msa.business.domain.model.Business;

public interface BusinessRepository {

    boolean existsByNameAndDeletedAtIsNull(String name);

    void save(Business business);
}
