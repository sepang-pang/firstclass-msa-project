package com.first_class.msa.business.domain.repository;

import com.first_class.msa.business.domain.model.Business;

import java.util.Optional;

public interface BusinessRepository {

    boolean existsByNameAndDeletedAtIsNull(String name);

    Optional<Business> findByIdAndDeletedAtIsNull(Long id);

    void save(Business business);
}
