package com.first_class.msa.product.infrastructure.repository;

import com.first_class.msa.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public boolean existsByNameAndBusinessIdAndDeletedAtIsNull(String name, Long businessId) {
        return jpaProductRepository.existsByNameAndBusinessIdAndDeletedAtIsNull(name, businessId);
    }
}
