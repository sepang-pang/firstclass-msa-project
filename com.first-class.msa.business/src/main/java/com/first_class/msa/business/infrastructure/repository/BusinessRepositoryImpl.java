package com.first_class.msa.business.infrastructure.repository;

import com.first_class.msa.business.domain.model.Business;
import com.first_class.msa.business.domain.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BusinessRepositoryImpl implements BusinessRepository {

    private final JpaBusinessRepository jpaBusinessRepository;

    @Override
    public boolean existsByNameAndDeletedAtIsNull(String name) {
        return jpaBusinessRepository.existsByNameAndDeletedAtIsNull(name);
    }

    @Override
    public void save(Business business) {
        jpaBusinessRepository.save(business);
    }
}
