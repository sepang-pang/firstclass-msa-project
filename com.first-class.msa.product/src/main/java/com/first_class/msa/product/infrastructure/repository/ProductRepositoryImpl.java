package com.first_class.msa.product.infrastructure.repository;

import com.first_class.msa.product.domain.model.Product;
import com.first_class.msa.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public boolean existsByNameAndBusinessIdAndDeletedAtIsNull(String name, Long businessId) {
        return jpaProductRepository.existsByNameAndBusinessIdAndDeletedAtIsNull(name, businessId);
    }

    @Override
    public Optional<Product> findByIdAndDeletedAtIsNull(Long productId) {
        return jpaProductRepository.findByIdAndDeletedAtIsNull(productId);
    }

    @Override
    public void save(Product product) {
        jpaProductRepository.save(product);
    }
}
