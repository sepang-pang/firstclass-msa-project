package com.first_class.msa.business.infrastructure.repository;

import com.first_class.msa.business.domain.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaBusinessRepository extends JpaRepository<Business, Long> {

    boolean existsByNameAndDeletedAtIsNull(String name);

    boolean existsByIdAndDeletedAtIsNull(Long id);

    Optional<Business> findByIdAndDeletedAtIsNull(Long id);
}
