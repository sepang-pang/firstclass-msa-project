package com.first_class.msa.product.domain.repository;

import com.first_class.msa.product.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    boolean existsByNameAndBusinessIdAndDeletedAtIsNull(String name, Long businessId);

    Optional<Product> findByIdAndDeletedAtIsNull(Long productId);

    Page<Product> findProductByDeletedAtIsNullWithConditions(Long userId, Pageable pageable, String name, Integer minPrice, Integer maxPrice, String sort);

    List<Product> findByIdInAndDeletedAtIsNull(List<Long> productIdList);

    void save(Product product);
}
