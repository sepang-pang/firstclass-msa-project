package com.first_class.msa.orders.infrastructure.repository;

import org.springframework.data.domain.Pageable;

import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;

public interface OrderQueryRepository {

	ResOrderSearchDTO findAll(Long userId, Pageable pageable);

}
