package com.first_class.msa.business.infrastructure.repository;

import com.first_class.msa.business.domain.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBusinessRepository extends JpaRepository<Business, Long> {

    boolean existsByNameAndDeletedAtIsNull(String name);
}
