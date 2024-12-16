package com.first_class.msa.business.domain.repository;

import com.first_class.msa.business.domain.model.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BusinessRepository {

    boolean existsByNameAndDeletedAtIsNull(String name);

    boolean existsByIdAndDeletedAtIsNull(Long id);

    Optional<Business> findByIdAndDeletedAtIsNull(Long id);

    Optional<Business> findByManagerIdAndDeletedAtIsNull(Long managerId);

    Page<Business> findBusinessByDeletedAtIsNullWithConditions(Pageable pageable, String name, String address, String type, String sort);

    void save(Business business);
}
