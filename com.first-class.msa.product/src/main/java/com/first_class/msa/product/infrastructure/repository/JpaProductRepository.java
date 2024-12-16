package com.first_class.msa.product.infrastructure.repository;

import com.first_class.msa.product.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameAndBusinessIdAndDeletedAtIsNull(String name, Long businessId);

    Optional<Product> findByIdAndDeletedAtIsNull(Long productId);

    List<Product> findByIdInAndDeletedAtIsNull(List<Long> productIdList);
}
