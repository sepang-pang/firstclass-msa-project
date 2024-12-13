package com.first_class.msa.product.domain.repository;

import com.first_class.msa.product.domain.model.Product;

public interface ProductRepository {

    boolean existsByNameAndBusinessIdAndDeletedAtIsNull(String name, Long businessId);

    void save(Product product);
}
