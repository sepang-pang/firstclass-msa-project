package com.first_class.msa.product.domain.repository;

import com.first_class.msa.product.domain.model.Product;

import java.util.Optional;

public interface ProductRepository {

    boolean existsByNameAndBusinessIdAndDeletedAtIsNull(String name, Long businessId);

    Optional<Product> findByIdAndDeletedAtIsNull(Long productId);

    void save(Product product);
}
