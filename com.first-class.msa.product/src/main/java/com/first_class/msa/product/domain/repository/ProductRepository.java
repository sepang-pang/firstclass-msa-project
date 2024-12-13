package com.first_class.msa.product.domain.repository;

public interface ProductRepository {

    boolean existsByNameAndBusinessIdAndDeletedAtIsNull(String name, Long businessId);
}
