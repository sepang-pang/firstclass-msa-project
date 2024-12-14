package com.first_class.msa.product.domain.repository;

import com.first_class.msa.product.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {

    boolean existsByNameAndBusinessIdAndDeletedAtIsNull(String name, Long businessId);

    Optional<Product> findByIdAndDeletedAtIsNull(Long productId);

    Page<Product> findProductByDeletedAtIsNullWithConditions(Long userId, Pageable pageable, String name, Integer minPrice, Integer maxPrice, String sort);

    void save(Product product);
}
