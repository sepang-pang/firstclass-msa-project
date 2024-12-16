package com.first_class.msa.product.infrastructure.repository;

import com.first_class.msa.product.domain.model.Product;
import com.first_class.msa.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;
    private final ProductQueryRepository productQueryRepository;

    @Override
    public boolean existsByNameAndBusinessIdAndDeletedAtIsNull(String name, Long businessId) {
        return jpaProductRepository.existsByNameAndBusinessIdAndDeletedAtIsNull(name, businessId);
    }

    @Override
    public Optional<Product> findByIdAndDeletedAtIsNull(Long productId) {
        return jpaProductRepository.findByIdAndDeletedAtIsNull(productId);
    }

    @Override
    public Page<Product> findProductByDeletedAtIsNullWithConditions(Long userId, Pageable pageable, String name, Integer minPrice, Integer maxPrice, String sort) {
        return productQueryRepository.findProductByDeletedAtIsNullWithConditions(userId, pageable, name, minPrice, maxPrice, sort);
    }

    @Override
    public List<Product> findByIdInAndDeletedAtIsNull(List<Long> productIdList) {
        return jpaProductRepository.findByIdInAndDeletedAtIsNull(productIdList);
    }

    @Override
    public void save(Product product) {
        jpaProductRepository.save(product);
    }
}
