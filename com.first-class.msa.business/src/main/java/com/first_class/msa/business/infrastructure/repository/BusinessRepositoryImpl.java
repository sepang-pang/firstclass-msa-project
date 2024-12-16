package com.first_class.msa.business.infrastructure.repository;

import com.first_class.msa.business.domain.model.Business;
import com.first_class.msa.business.domain.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BusinessRepositoryImpl implements BusinessRepository {

    private final JpaBusinessRepository jpaBusinessRepository;
    private final BusinessQueryRepository businessQueryRepository;

    @Override
    public boolean existsByNameAndDeletedAtIsNull(String name) {
        return jpaBusinessRepository.existsByNameAndDeletedAtIsNull(name);
    }

    @Override
    public boolean existsByIdAndDeletedAtIsNull(Long id) {
        return jpaBusinessRepository.existsByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Optional<Business> findByIdAndDeletedAtIsNull(Long id) {
        return jpaBusinessRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Optional<Business> findByManagerIdAndDeletedAtIsNull(Long managerId) {
        return jpaBusinessRepository.findByManagerIdAndDeletedAtIsNull(managerId);
    }

    @Override
    public Page<Business> findBusinessByDeletedAtIsNullWithConditions(Pageable pageable, String name, String address, String type, String sort) {

        return businessQueryRepository.findBusinessByDeletedAtIsNullWithConditions(pageable, name, address, type, sort);
    }

    @Override
    public void save(Business business) {
        jpaBusinessRepository.save(business);
    }
}
